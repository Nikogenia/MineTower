'use client'
import { useContext, useEffect, useState } from "react"
import { MainContext } from "@/components/MainContext"
import { getUser, getUsers, registerUser, unregisterUser } from "@/utils/api"
import { useRouter } from "next/navigation"
import { toast } from "react-toastify"
import Loading from "@/components/Loading"
import AccessDenied from "@/components/AccessDenied"
import { MdDelete } from "react-icons/md"
  
export default function AdminUser() {

  const {setTitle, user, setUser, backend} = useContext(MainContext)
  const router = useRouter()

  const [users, setUsers] = useState([])

  useEffect(() => {
    setTitle("Manage Users")
    if (user.name == "") getUser(backend, router, setUser, true)
    getUsers(backend, setUsers)
  }, [])

  if (user.name == "") {
    return (
      <Loading />
    )
  }

  if (!user.admin) {
    return (
      <AccessDenied />
    )
  }

  return (
    <div className="h-full flex flex-col md:flex-row items-center overflow-y-auto gap-6 p-6">
      <Register setUsers={setUsers} />
      <Users users={users} setUsers={setUsers} username={user.name}/>
    </div>
  )

}

function Register({setUsers}) {

    const {backend} = useContext(MainContext)

    const [inputUsername, setInputUsername] = useState("")
    const [inputPassword, setInputPassword] = useState("")
    const [inputAdmin, setInputAdmin] = useState(false)

    const submit = async (e) => {
        e.preventDefault()
        if (inputUsername.length == 0) {
          toast.error("Register failed (empty_field): The username field is empty!")
          return
        }
        if (inputPassword.length == 0) {
          toast.error("Register failed (empty_field): The password field is empty!")
          return
        }
        if (inputPassword.length < 4) {
          toast.error("Register failed (too_short): The password need to at least 4 characters long!")
          return
        }
        await registerUser(backend, inputUsername, inputPassword, inputAdmin)
        getUsers(backend, setUsers)
    }

    return (
        <form onSubmit={submit} action=""
        className="bg-bg-primary p-4 rounded-lg flex flex-col items-center justify-center w-full md:w-[20rem] min-h-[27rem] md:h-full">
          <div className="text-4xl text-center mb-8">Register</div>
          <div className="flex flex-col items-center gap-2 mb-6 w-full">
            <div className="text-xl text-center">Username</div>
            <input type="text" value={inputUsername} onChange={(e) => setInputUsername(e.target.value)} placeholder="admin" autoFocus
            className="bg-bg-secondary p-1 rounded-lg border-fg-secondary hover:brightness-110
            form-input w-full placeholder-bg-neutral" autoComplete="off" />
          </div>
          <div className="flex flex-col items-center gap-2 mb-6 w-full">
            <div className="text-xl text-center">Password</div>
            <input type="password" value={inputPassword} onChange={(e) => setInputPassword(e.target.value)} placeholder="1234"
            className="bg-bg-secondary p-1 rounded-lg border-fg-secondary hover:brightness-110
            form-input w-full placeholder-bg-neutral" autoComplete="off" />
          </div>
          <div className="flex items-center justify-center gap-2 mb-8 w-full">
            <div className="text-xl">Admin</div>
            <input type="checkbox" checked={inputAdmin} onChange={(e) => setInputAdmin(e.target.checked)}
            className="bg-bg-secondary rounded-md border-fg-secondary hover:brightness-110
            form-checkbox" />
          </div>
          <button className="bg-accent text-bg-neutral text-xl py-2 px-8 rounded-lg focus:outline-1
          focus:outline-fg-primary hover:scale-105">Register</button>
        </form>
    )

}

function Users({users, setUsers, username}) {

  const {backend} = useContext(MainContext)

  const unregister = async (username) => {
    await unregisterUser(backend, username)
    getUsers(backend, setUsers)
  }

  return (
    <div className="bg-bg-primary p-4 w-full h-full rounded-lg flex flex-col items-center">
      <div className="text-4xl text-center mb-8">Users</div>
      <div className="flex flex-col items-center w-full md:overflow-y-auto">
        {
          users.map((user, i) => {return (
            <div key={i} className="w-full">
              <div className="bg-bg-secondary mx-3 mb-3 rounded-xl flex justify-between items-center">
                  <div className="flex items-center">
                      <div className="text-xl py-2 pl-3">{user.name}</div>
                      {(user.admin) ? (
                        <div className="text-lg text-red-100 bg-red-700 font-light font-mono px-1 rounded-xl ml-2">ADMIN</div>
                      ) : <></>}
                  </div>
                  {(user.name == "admin" | user.name == username) ? <></> : (
                      <button className="bg-red-500 rounded mr-3 px-2 py-1 text-xl hover:scale-105"
                      onClick={() => unregister(backend, user.name)}><MdDelete /></button>
                  )}
              </div>
            </div>
          )})
        }
      </div>
    </div>
  )

}
