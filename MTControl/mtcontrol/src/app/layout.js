'use client'
import { useState } from 'react'
import Footer from './Footer'
import Header from './Header'
import './globals.css'
import { MainContext } from './MainContext'

export const metadata = {
  title: 'MineTower',
  description: 'MineTower Console'
}

export default function RootLayout({children}) {

  const [title, setTitle] = useState("")
  const [user, setUser] = useState({
    name: "Nikogenia",
    admin: true
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
          <main className='flex flex-col h-screen bg-neutral'>
            <Header />
            {children}
            <Footer />
          </main>
        </MainContext.Provider>
      </body>
    </html>
  )

}
