'use client'
import { useContext, useEffect, useState } from "react"
import { MainContext } from "@/components/MainContext"
import { logout } from "@/utils/api"
import { useRouter } from "next/navigation"
import Loading from "@/components/Loading"

export default function Logout() {

  const {setTitle, setUser} = useContext(MainContext)
  const router = useRouter()

  useEffect(() => {
    setTitle("Logout")
    logout(router, setUser)
  }, [])

  return (
    <Loading />
  )

}
