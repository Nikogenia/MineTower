"use client"
import { ToastContainer } from "react-toastify";
import { MainContext } from "./MainContext";
import Header from "./Header";
import Footer from "./Footer";
import { useState } from "react";

export default function Root({children}) {

    const [title, setTitle] = useState("")
    const [user, setUser] = useState({
      name: "",
      admin: false
    })
  
    const state = {
      title: title,
      setTitle: setTitle,
      user: user,
      setUser: setUser
    }

    return (
      <MainContext.Provider value={state}>
        <main className='flex flex-col screen-height bg-bg-neutral text-fg-primary'>
          <ToastContainer position="top-right" theme='colored'/>
          <Header />
          {children}
          <Footer />
        </main>
      </MainContext.Provider>
    )

}
