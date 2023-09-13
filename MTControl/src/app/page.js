'use client'
import { useContext, useEffect } from "react"
import { MainContext } from "./MainContext"

export default function Home() {

  const {title, setTitle} = useContext(MainContext)

  useEffect(() => {
    setTitle("")
  }, [])

  return (
    <div className="h-full">Moin {title}</div>
  )

}
