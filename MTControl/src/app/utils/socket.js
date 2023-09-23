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

    console.info("Got logs")
    setLogs(data)

}

function onAgents(data, setAgents) {

    console.info("Got agents")
    setAgents(data.agents)

}

function onServers(data, setServers) {

    console.info("Got servers")
    setServers(data.servers)

}

function onLineUpdate(data, setLogs) {

    console.info("Got line update")
    setLogs(prev => {
        if (!(data.server in prev)) {
            prev[data.server] = ""
        }
        prev[data.server] = prev[data.server] + data.line + "\n"
        return prev
    })

}

export async function manageSocket(socket, setSocket, setAgents, setServers, setLogs) {
    
    if (socket == null) {
        console.info("Connect to socket")
        setSocket(io(BACKEND + "/control", {
            withCredentials: true,
        }))
        return
    }

    socket.on("connect", () => onConnect(socket))

    socket.on("disconnect", () => onDisconnect(socket))
    
    socket.on("logs", (data) => onLogs(data, setLogs))

    socket.on("servers", (data) => onServers(data, setServers))

    socket.on("agents", (data) => onAgents(data, setAgents))

    socket.on("line_update", (data) => onLineUpdate(data, setLogs))

    socket.on("control_error", (data) => {
        console.error("Socket error: " + data.error)
        toast.error("API socket error (" + data.error + "): " + data.message)
    })

}
