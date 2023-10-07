import { useContext, useState } from "react"
import { MainContext } from "@/components/MainContext"
import Link from "next/link"
import { MdClose, MdLogout, MdManageAccounts, MdMenu, MdStar } from "react-icons/md"
import { TbBrandPowershell } from "react-icons/tb"
import Image from "next/image"
import { getTitle } from "@/utils/api"

export default function Header() {

    const {title, user} = useContext(MainContext)
    const [menuOpen, setMenuOpen] = useState(false)

    const toggleMenu = () => {
        setMenuOpen(value => {
            return !value
        })
    }
    
    return (
        <header className="bg-bg-primary flex py-2 px-4 items-center justify-between">
            <Title title={title}/>
            {(user.name != "") ? (
                <>
                <div className="items-center hidden sm:flex">
                    <Pages user={user}/>
                    <User user={user} />
                    <Logout />
                </div>
                <button className="block sm:hidden" onClick={toggleMenu}>
                    {(menuOpen) ? (
                        <MdClose className="text-3xl"/>
                    ) : (
                        <MdMenu className="text-3xl"/>
                    )}
                </button>
                </>
            ) : <></>}
        </header>
    )

}

function Title({title}) {

    const {backend} = useContext(MainContext)

    return (
        <div className="flex items-center justify-center">
            <Link href="/" className="hover:scale-105">
                <div className="flex items-center justify-center">
                    <Image src="/android-chrome-192x192.png" width="0" height="0" sizes="100vw" className="w-8 h-8" alt="icon" priority={true} />
                    {(title == "") ? (
                        <div className="text-2xl font-bold ml-2">{getTitle(backend)}</div>
                    ) : (
                        <div className="text-2xl font-bold ml-2 hidden sm:block">{getTitle(backend)}</div>
                    )}
                </div>
            </Link>
            <div className="text-xl font-thin ml-3">{title}</div>
        </div>
    )

}

function User({user}) {

    return (
        <Link href="/user" className="hover:scale-110 group">
            <div className="flex items-center justify-center ml-3">
                <div className="text-xl font-semibold">{user.name}</div>
                {(user.admin) ? (
                    <MdStar className="text-xl ml-1" />
                ) : <></>}
            </div>
        </Link>
    )

}

function Logout() {

    return (
        <Link href="/user/logout" className="group">
            <MdLogout className="text-2xl ml-3 hover:scale-110" />
        </Link>
    )

}

function Pages({user}) {

    return (
        <div className="flex items-center justify-center">
        <Link href="/console" className="relative group">
            <TbBrandPowershell className="text-2xl ml-3 hover:scale-110" />
            <div className="absolute top-11 -translate-x-[20%] bg-bg-secondary py-1 px-2
            rounded-lg scale-0 group-hover:scale-100">Console</div>
        </Link>
        {(user.admin) ? (
            <Link href="/admin/user" className="relative group">
                <MdManageAccounts className="text-2xl ml-3 hover:scale-110" />
                <div className="absolute top-11 -translate-x-[30%] bg-bg-secondary py-1 px-2
                rounded-lg scale-0 group-hover:scale-100 whitespace-nowrap">Manage Users</div>
            </Link>
        ) : <></>}
        </div>
    )

}
