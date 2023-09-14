'use client'
import { useState } from 'react'
import Footer from '@/components/Footer'
import Header from '@/components/Header'
import './globals.css'
import { MainContext } from '@/MainContext'
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

export const metadata = {
  title: 'MineTower',
  description: 'MineTower Console'
}

export default function RootLayout({children}) {

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
    <html lang="en" className='md:text-lg'>
      <body>
        <MainContext.Provider value={state}>
          <main className='flex flex-col screen-height bg-bg-neutral text-fg-primary'>
            <ToastContainer position="top-right" theme='colored'/>
            <Header />
            {children}
            <Footer />
          </main>
        </MainContext.Provider>
      </body>
    </html>
  )

}
