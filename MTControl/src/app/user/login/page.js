'use client'
import { useContext, useEffect, useState } from "react"
import { MainContext } from "@/components/MainContext"
import { login, checkUser } from "@/utils/api"
import { useRouter } from "next/navigation"
import { toast } from "react-toastify"

export default function Login() {

  const {setTitle, user, setUser} = useContext(MainContext)
  const router = useRouter()
  const [inputUsername, setInputUsername] = useState("")
  const [inputPassword, setInputPassword] = useState("")

  useEffect(() => {
    setTitle("Login")
    if (user.name == "") checkUser(router, setUser)
    else router.push("/")
  }, [])

  const submit = (e) => {
    e.preventDefault()
    if (inputUsername.length == 0) {
      toast.error("Login failed (empty_field): The username field is empty!")
      return
    }
    if (inputPassword.length == 0) {
      toast.error("Login failed (empty_field): The password field is empty!")
      return
    }
    login(router, inputUsername, inputPassword)
  }

  return (
    <div className="h-full flex items-center justify-center">
      <form onSubmit={submit} action=""
      className="bg-bg-primary p-4 rounded-lg flex flex-col items-center justify-center w-[32rem] max-w-[90%] h-[26rem] sm:h-[19rem] max-h-[90%]">
        <div className="text-4xl text-center mb-8">Login</div>
        <div className="flex flex-col sm:flex-row items-center gap-4 mb-6 w-full">
          <div className="text-xl w-32 text-center sm:text-right">Username</div>
          <input type="text" value={inputUsername} onChange={(e) => setInputUsername(e.target.value)} placeholder="admin" autoFocus
          className="bg-bg-secondary p-1 rounded-lg border-fg-secondary hover:brightness-110
          form-input w-full placeholder-bg-neutral" autoComplete="username" />
        </div>
        <div className="flex flex-col sm:flex-row items-center gap-4 mb-8 w-full">
          <div className="text-xl w-32 text-center sm:text-right">Password</div>
          <input type="password" value={inputPassword} onChange={(e) => setInputPassword(e.target.value)} placeholder="1234"
          className="bg-bg-secondary p-1 rounded-lg border-fg-secondary hover:brightness-110
          form-input w-full placeholder-bg-neutral" autoComplete="current-password" />
        </div>
        <button className="bg-accent text-bg-neutral text-xl py-2 px-16 rounded-lg focus:outline-1
        focus:outline-fg-primary hover:scale-105">Login</button>
      </form>
    </div>
  )

}
