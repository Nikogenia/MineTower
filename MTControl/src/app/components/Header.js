import { useContext } from "react"
import { MainContext } from "@/MainContext"
import Link from "next/link"
import { MdLogout, MdStar } from "react-icons/md"
import Image from "next/image"
import { getTitle } from "@/utils/api"

export default function Header() {

    const {title, user} = useContext(MainContext)
    
    return (
        <header className="bg-bg-primary flex py-2 px-4 items-center justify-between">
            <Title title={title}/>
            {(user.name != "") ? (
                <div className="flex items-center">
                    <User user={user}/>
                    <Logout/>
                </div>
            ) : <></>}
        </header>
    )

}

function Title({title}) {

    return (
        <div className="flex items-center justify-center">
            <Link href="/">
                <div className="flex items-center justify-center">
                    <Image src="/AppIcon.png" width="0" height="0" sizes="100vw" className="w-8 h-8" alt="icon" />
                    <div className="text-2xl font-bold ml-2">{getTitle()}</div>
                </div>
            </Link>
            <div className="text-2xl font-light ml-3">{title}</div>
        </div>
    )

}

function User({user}) {

    return (
        <Link href="/user">
            <div className="flex items-center justify-center">
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
        <Link href="/user/logout">
            <MdLogout className="text-2xl ml-3 hover:scale-125" />
        </Link>
    )

}
