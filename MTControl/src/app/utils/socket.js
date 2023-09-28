import { toast } from "react-toastify"
import { BACKEND } from "./api"
import { io } from "socket.io-client"

function onConnect(socket) {

    console.info("Socket connected")
    socket.emit("agents", {})
    socket.emit("servers", {})
    socket.emit("logs", {})

}

function onDisconnect(socket) {

    console.info("Socket disconnected")

}

function onLogs(data, setLogs) {

    console.debug("Got logs")
    setLogs(data)

}

function onAgents(data, setAgents) {

    console.debug("Got agents")
    setAgents(data.agents)

}

function onServers(data, setServers) {

    console.debug("Got servers")
    setServers(data.servers)

}

function onLogUpdate(data, setLogs) {

    console.debug("Got log update")
    setLogs(prev => {
        return {...prev, [data.server]: prev[data.server] + data.data}
    })

}

function onTabComplete(data, setOptions) {

    console.debug("Got tab complete")
    setOptions(prev => {
        return {...prev, options: data.options, input: data.input, index: 0, update: true}
    })

}

export async function changeMode(socket, server, mode) {

    console.debug("Change mode")
    toast.info("Changing mode! Please wait!")
    socket.emit("change_mode", {
        server: server,
        mode: mode
    })

}

export async function command(socket, server, command) {

    console.debug("Command")
    socket.emit("command", {
        server: server,
        command: command
    })

}

export async function tabComplete(socket, server, input) {

    console.debug("Tab complete")
    socket.emit("tab_complete", {
        server: server,
        input: input
    })

}

export async function manageSocket(socket, setSocket, setAgents, setServers, setLogs, setOptions) {
    
    if (socket == null) {
        console.info("Connect to socket")
        setSocket(io(BACKEND + "/control", {
            withCredentials: true,
        }))
        return
    }

    socket.removeAllListeners()

    socket.on("connect", () => onConnect(socket))

    socket.on("disconnect", () => onDisconnect(socket))
    
    socket.on("logs", (data) => onLogs(data, setLogs))

    socket.on("servers", (data) => onServers(data, setServers))

    socket.on("agents", (data) => onAgents(data, setAgents))

    socket.on("log_update", (data) => onLogUpdate(data, setLogs))

    socket.on("tab_complete", (data) => onTabComplete(data, setOptions))

    socket.on("control_error", (data) => {
        console.error("Socket error: " + data.error)
        toast.error("API socket error (" + data.error + "): " + data.message)
    })

}
