'use client'
import { useCallback, useContext, useEffect, useRef, useState } from "react"
import { MainContext } from "@/components/MainContext"
import { getUser } from "@/utils/api"
import { usePathname, useRouter, useSearchParams } from "next/navigation"
import Loading from "@/components/Loading"
import { manageSocket } from "@/utils/socket"
import { MdChevronRight, MdExpandMore } from "react-icons/md"

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

  console.log(servers, logs, agents)

  useEffect(() => {
    setTitle("Console")
    if (user.name == "") getUser(router, setUser, true)
  }, [])

  useEffect(() => {
    manageSocket(socket, setSocket, setAgents, setServers, setLogs)
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
      <Control servers={servers} selected={selected} logs={logs} />
    </div>
  )

}

function Servers({agents, servers, showAgents, setShowAgents, selected, setSelected}) {

  return (
    <div className="bg-bg-primary py-4 w-full md:w-[20rem] md:h-full rounded-lg flex flex-col items-center">
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
    <div className="w-[90%] rounded-lg overflow-auto">
      <button className="flex items-center bg-bg-neutral w-full"
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
    <button className={(selected == server.name) ? "flex items-center bg-accent text-bg-neutral w-full" : "flex items-center bg-bg-secondary w-full"}
        onClick={() => {
          setSelected(server.name)
        }}>
        <div className="text-xl py-1 pl-2">{server.name}</div>
    </button>
  )

}

function Control({servers, selected, logs}) {

  const output = useRef()

  const [autoScroll, setAutoScroll] = useState(true)

  useEffect(() => {
    if (output.current == null) return
    if (selected in logs) output.current.value = logs[selected]
  }, [logs, selected])

  if (selected == "") return (
    <div className="bg-bg-primary p-4 w-full h-full rounded-lg flex flex-col justify-center">
      <div className="text-4xl text-center">Nothing selected</div>
    </div>
  )

  return (
    <div className="bg-bg-primary p-4 w-full h-full rounded-lg flex flex-col items-center">
      <div className="text-3xl bg-bg-secondary rounded-lg py-1 px-2 text-center font-mono mb-4">{selected}</div>
      <textarea className="bg-bg-secondary rounded-lg font-mono text-sm
        border-fg-secondary hover:brightness-110 resize-none h-full w-full p-1"
        wrap="hard"
        ref={output}
        readOnly></textarea>
    </div>
  )

}
