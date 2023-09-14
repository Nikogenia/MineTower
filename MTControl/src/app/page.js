'use client'
import { useContext, useEffect } from "react"
import { MainContext } from "@/MainContext"
import { PulseLoader } from "react-spinners"
import tailwindConfig from "../../tailwind.config"
import { getUser } from "./utils/api"
import { useRouter } from "next/navigation"

export default function Home() {

  const {setTitle, user, setUser} = useContext(MainContext)
  const router = useRouter()

  useEffect(() => {
    setTitle("")
    if (user.name == "") getUser(router, setUser, true)
  }, [])

  if (user.name == "") {
    return (
      <div className="h-full flex items-center justify-center">
        <PulseLoader color={tailwindConfig.theme.extend.colors["fg-primary"]} />
      </div>
    )
  }

  return (
    <div className="h-full flex items-center justify-center">
      <div>Moin</div>
    </div>
  )

}
