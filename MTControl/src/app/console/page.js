'use client'
import { useCallback, useContext, useEffect, useRef, useState } from "react"
import { MainContext } from "@/components/MainContext"
import { getUser } from "@/utils/api"
import { usePathname, useRouter, useSearchParams } from "next/navigation"
import Loading from "@/components/Loading"
import { changeMode, command, manageSocket, tabComplete } from "@/utils/socket"
import { MdChevronRight, MdExpandMore, MdSend } from "react-icons/md"

export default function Console() {

  const {setTitle, user, setUser} = useContext(MainContext)

  const router = useRouter()
  const pathname = usePathname()
  const searchParams = useSearchParams()

  const createQueryString = useCallback(
    (name, value) => {
      const params = new URLSearchParams(searchParams)
      params.set(name, value)
      return params.toString()
    },
    [searchParams]
  )

  let selected = ""
  if (searchParams.has("server"))
    selected = searchParams.get("server")

  let showAgents = []
  if (searchParams.has("agent"))
    showAgents = searchParams.get("agent").split(",")
  if (showAgents[0] == "") showAgents = []

  const setShowAgents = (value) => {
    router.replace(pathname + '?' + createQueryString('agent', value))
  }

  const setSelected = (value) => {
    router.replace(pathname + '?' + createQueryString('server', value))
  }

  const [servers, setServers] = useState([])
  const [agents, setAgents] = useState([])
  const [logs, setLogs] = useState({})
  const [socket, setSocket] = useState(null)
  const [options, setOptions] = useState({
    index: 0,
    options: [],
    input: "",
    raw_input: "",
    update: false
  })

  useEffect(() => {
    setTitle("Console")
    if (user.name == "") getUser(router, setUser, true)
  }, [])

  useEffect(() => {
    manageSocket(socket, setSocket, setAgents, setServers, setLogs, setOptions)
    return () => {
      if (socket != null) socket.close()
    }
  }, [socket])

  if (user.name == "") {
    return (
      <Loading />
    )
  }

  return (
    <div className="h-full flex flex-col md:flex-row items-center overflow-y-auto md:overflow-x-visible gap-6 p-6">
      <Servers agents={agents} servers={servers} showAgents={showAgents} setShowAgents={setShowAgents}
      selected={selected} setSelected={setSelected} />
      <Control servers={servers} selected={selected} logs={logs} options={options} socket={socket} setOptions={setOptions} />
    </div>
  )

}

function Servers({agents, servers, showAgents, setShowAgents, selected, setSelected}) {

  return (
    <div className="bg-bg-primary p-4 w-full md:w-[20rem] md:h-full rounded-lg flex flex-col items-center">
      <div className="text-4xl text-center mb-4">Servers</div>
      <div className="flex flex-col items-center w-full md:overflow-y-auto mb-2">
        {
          agents.map((agent, i) => <Agent key={i} agent={agent} servers={servers}
          showAgents={showAgents} setShowAgents={setShowAgents} selected={selected} setSelected={setSelected} />)
        }
      </div>
    </div>
  )

}

function Agent({agent, servers, showAgents, setShowAgents, selected, setSelected}) {

  const children = []
  servers.forEach(server => {
    if (server.agent == agent.name) children.push(server)
  })

  return (
    <div className="w-full rounded-lg overflow-auto">
      <button className="flex items-center bg-bg-neutral hover:brightness-110 w-full"
          onClick={() => {
            if (showAgents.includes(agent.name))
              setShowAgents(showAgents.filter(e => e !== agent.name))
            else
              setShowAgents([...showAgents, agent.name])
          }}>
          {(showAgents.includes(agent.name)) ? (
              <MdExpandMore className="text-3xl" />) : (
              <MdChevronRight className="text-3xl" />)}
          <div className="text-xl py-1">{agent.name}</div>
      </button>
      {(showAgents.includes(agent.name)) ? (
          children.map(server => <Server key={server.name} server={server} selected={selected} setSelected={setSelected} />)) : <></>
      }
    </div>
  )

}

function Server({server, selected, setSelected}) {

  return (
    <button className={(selected == server.name) ? (
      "flex items-center bg-accent text-bg-neutral hover:brightness-110 w-full") : (
      "flex items-center bg-bg-secondary hover:brightness-110 w-full")}
      onClick={() => {
        setSelected(server.name)
      }}>
      <div className="text-xl py-1 pl-2">{server.name}</div>
    </button>
  )

}

function Control({servers, selected, logs, options, socket, setOptions}) {

  const output = useRef()

  const [autoScroll, setAutoScroll] = useState(true)
  const [input, setInput] = useState("")

  let server = null
  if (selected in logs) {
    for (const s of servers) {
      if (s.name == selected) server = s
    }
  }

  const submit = (e) => {
    e.preventDefault()
    command(socket, server.name, input)
    setInput("")
  }

  const nextOption = () => {
    if (!options.options.length) return
    setInput(prev => {
      return prev.substring(0, prev.lastIndexOf(" ") + 1) + options.options[options.index]
    })
    setOptions(prev => {
      if (prev.index + 1 == prev.options.length) return {...prev, index: 0}
      return {...prev, index: prev.index + 1}
    })
  }

  const handleKeyPress = (e) => {
    if (e.key != "Tab") return
    e.preventDefault()
    if (options.input != options.raw_input | (options.input == "" & !options.options.length)) {
      tabComplete(socket, server.name, input)
      return
    }
    nextOption()
  }

  useEffect(() => {
    if (output.current == null) return
    if (selected in logs) output.current.value = logs[selected]
    if (autoScroll) output.current.scrollTop = output.current.scrollHeight
  }, [logs, selected])

  useEffect(() => {
    if (!options.update) return
    setOptions(prev => {
      return {...prev, update: false}
    })
    nextOption()
  }, [options])

  if (selected == "") return (
    <div className="bg-bg-primary p-4 w-full h-full rounded-lg flex flex-col justify-center">
      <div className="text-4xl text-center">Nothing selected</div>
    </div>
  )
  
  if (server == null) return (
    <div className="bg-bg-primary p-4 w-full h-full rounded-lg flex flex-col justify-center">
      <div className="text-4xl text-center">Server not found!</div>
    </div>
  )

  return (
    <div className="bg-bg-primary p-4 w-full h-full rounded-lg flex flex-col items-center">
      <div className="text-3xl bg-bg-secondary rounded-lg py-1 px-2 text-center font-mono mb-3">{server.name}</div>
      <textarea className="bg-bg-secondary rounded-lg font-mono text-xs
        border-fg-secondary resize-none h-[25rem] md:h-full w-full p-1"
        wrap="hard"
        ref={output}
        readOnly></textarea>
      {(server.mode == "off") ? (
        <div className="flex w-full gap-1 mt-2">
          <div className="bg-red-700 text-red-200 text-xl font-mono font-bold w-full
            rounded-md border-red-600 border-2 text-center px-3 pt-0.5">OFFLINE</div>
          <select value={server.mode} onChange={(e) => changeMode(socket, server.name, e.target.value)} className="text-xl rounded-md bg-accent text-bg-neutral
            px-3 py-0.5 border border-bg-neutral hover:brightness-110 font-semibold">
            {[
              ['off', 'Off'],
              ['manual', 'Manual'],
              ['failure', 'Restart Failure'],
              ['always', 'Restart Always'],
            ].map(([value, name]) => (
              <option key={value} value={value} className="text-xl rounded bg-accent text-bg-neutral
              font-semibold border border-bg-neutral hover:brightness-110">{name}</option>
            ))}
          </select>
        </div>
      ) : (
        <div className="flex flex-col xl:flex-row w-full gap-1 mt-2">
          <form className="flex items-center w-full" onSubmit={submit} action="">
            <div className="bg-bg-secondary text-fg-secondary text-xl font-mono font-semibold px-2 pt-0.5
              rounded-md border-fg-secondary border-solid border mr-1">/</div>
            <input className="bg-bg-secondary rounded-md text-xl font-mono font-light px-1 pb-0 pt-0.5 mr-1
              hover:brightness-110 w-full placeholder-bg-neutral border-fg-secondary"
              type="text" value={input} autoFocus
              placeholder="help"
              onChange={(e) => setInput(e.target.value)}
              onInput={(e) => setOptions(prev => {return {...prev, raw_input: e.target.value}})}
              onKeyDown={handleKeyPress}
              autoComplete="off" ></input>
            <button className="bg-accent rounded-md px-1 h-full
              text-bg-neutral text-2xl hover:brightness-110 border border-bg-neutral"
              ><MdSend /></button>
          </form>
          <div className="flex gap-1">
            <div className="bg-lime-700 text-lime-200 text-xl font-mono font-bold w-full xl:w-auto
              rounded-md border-lime-600 border-2 text-center px-3 pt-0.5">ONLINE</div>
            <select value={server.mode} onChange={(e) => changeMode(socket, server.name, e.target.value)} className="text-xl rounded-md bg-accent text-bg-neutral
              px-3 py-0.5 border border-bg-neutral hover:brightness-110 font-semibold">
              {[
                ['off', 'Off'],
                ['manual', 'Manual'],
                ['failure', 'Restart Failure'],
                ['always', 'Restart Always'],
              ].map(([value, name]) => (
                <option key={value} value={value} className="text-xl rounded bg-accent text-bg-neutral
                font-semibold border border-bg-neutral hover:brightness-110">{name}</option>
              ))}
            </select>
          </div>
        </div>
      )}
    </div>
  )

}
