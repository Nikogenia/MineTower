'use client'
import { useContext, useEffect, useState } from "react"
import { MainContext } from "@/MainContext"
import { request } from "@/utils/api"
import { useRouter } from "next/navigation"
import { toast } from "react-toastify"
import tailwindConfig from "../../../../tailwind.config"
import { PulseLoader } from "react-spinners"

async function logout(router, setUser) {

  console.info("Logout")

  setUser({
    name: "",
    admin: false
  })

  const data = await request("/user/logout", {}, true)
  if (data == null) return

  if (data.error == "success") {
      console.info("Log out successful")
      console.info("Redirect to login")
      router.push("/user/login")
      return
  }

  console.log("Logout failed: " + data.error)
  toast.error("Logout failed (" + data.error + "): " + data.message)
  router.push("/user/login")

}

export default function Logout() {

  const {setTitle, setUser} = useContext(MainContext)
  const router = useRouter()

  useEffect(() => {
    setTitle("Logout")
    logout(router, setUser)
  }, [])

  return (
    <div className="h-full flex items-center justify-center">
      <PulseLoader color={tailwindConfig.theme.extend.colors["fg-primary"]} />
    </div>
  )

}
