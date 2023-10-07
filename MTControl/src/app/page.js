'use client'
import { useContext, useEffect } from "react"
import { MainContext } from "@/components/MainContext"
import { getUser } from "./utils/api"
import { useRouter } from "next/navigation"
import Loading from "./components/Loading"

export default function Home() {

  const {setTitle, user, setUser, backend} = useContext(MainContext)
  const router = useRouter()

  useEffect(() => {
    setTitle("")
    if (user.name == "") getUser(backend, router, setUser, true)
  }, [])

  if (user.name == "") {
    return (
      <Loading />
    )
  }

  return (
    <div className="h-full flex items-center justify-center">
      <div>Moin</div>
    </div>
  )

}
