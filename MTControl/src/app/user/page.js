'use client'
import { useContext, useEffect, useState } from "react"
import { MainContext } from "@/components/MainContext"
import { getUser, changePassword } from "@/utils/api"
import { useRouter } from "next/navigation"
import { toast } from "react-toastify"
import Loading from "@/components/Loading"
  
export default function User() {

  const {setTitle, user, setUser} = useContext(MainContext)
  const router = useRouter()
  const [inputPassword, setInputPassword] = useState("")

  useEffect(() => {
    setTitle("User")
    if (user.name == "") getUser(router, setUser, true)
  }, [])

  const submit = (e) => {
    e.preventDefault()
    if (inputPassword.length == 0) {
      toast.error("Password change failed (empty_field): The password field is empty!")
      return
    }
    if (inputPassword.length < 4) {
      toast.error("Password change failed (too_short): The password need to at least 4 characters long!")
      return
    }
    changePassword(user.name, inputPassword)
  }

  if (user.name == "") {
    return (
        <Loading />
    )
  }

  return (
    <div className="h-full flex items-center justify-center">
      <form onSubmit={submit} action=""
      className="bg-bg-primary p-4 rounded-lg flex flex-col items-center justify-center w-[32rem] max-w-[90%] h-[27rem] sm:h-[23rem] max-h-[90%]">
        <div className="text-4xl text-center mb-4">User</div>
        <div className="text-2xl text-center mb-2 font-mono bg-bg-secondary px-2 py-1 rounded-lg">{user.name}</div>
        {(user.admin) ? (
            <div className="text-xl text-center">You are an admin!</div>
        ) : <></>}
        <div className="flex flex-col sm:flex-row items-center gap-4 mt-8 mb-8 w-full">
          <div className="text-xl w-32 text-center sm:text-right">Password</div>
          <input type="password" value={inputPassword} onChange={(e) => setInputPassword(e.target.value)} placeholder="1234"
          className="bg-bg-secondary p-1 rounded-lg border-fg-secondary hover:brightness-110
          form-input w-full placeholder-bg-neutral" />
        </div>
        <button className="bg-accent text-bg-neutral text-xl py-2 px-8 rounded-lg focus:outline-1
        focus:outline-fg-primary hover:scale-105">Change Password</button>
      </form>
    </div>
  )

}
