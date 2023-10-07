'use client'
import { useContext, useEffect } from "react"
import { MainContext } from "@/components/MainContext"
import { getUser } from "./utils/api"
import { useRouter } from "next/navigation"
import Loading from "./components/Loading"
import Image from "next/image"
import Link from "next/link"
import { TbBrandPowershell } from "react-icons/tb"

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
      <div className="bg-bg-primary p-4 rounded-lg flex flex-col items-center justify-center w-[32rem] max-w-[90%] h-[27rem] max-h-[90%]">
        <Image src="/android-chrome-512x512.png" width="0" height="0" sizes="100vw" className="w-32 h-32" alt="icon" priority={true} />
        <div className="text-4xl text-center mt-4">Welcome!</div>
        <div className="text-2xl font-bold text-center mt-4">Get started</div>
        <Link href="/console" className="flex items-center bg-bg-secondary px-4 py-2 rounded-lg gap-2 hover:scale-110 mt-2">
            <TbBrandPowershell className="text-3xl" />
            <div className="text-2xl">Console</div>
        </Link>
        <div className="rounded-lg py-2 text-center bg-red-500 text-red-950 mt-8">
          If you just logged in the first time, please remember changing your password <Link href="/user" className="underline">here</Link>!
        </div>
      </div>
    </div>
  )

}
