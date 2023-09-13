import { useContext } from "react"
import { MainContext } from "@/MainContext"
import Link from "next/link"
import { MdStar } from "react-icons/md"

export default function Header() {

    const {title, user} = useContext(MainContext)
    
    return (
        <header className="bg-primary flex py-3 px-4 items-center justify-between">
            <Title title={title}/>
            {(user.name != "") ? (
                <User user={user}/>
            ) : <></>}
        </header>
    )

}

function Title({title}) {

    return (
        <div className="flex items-center justify-center">
            <Link href="/">
                <div className="text-secondary text-2xl font-bold pb-1">MineTower</div>
            </Link>
            <div className="text-secondary text-2xl font-light pb-1 pl-3">{title}</div>
        </div>
    )

}

function User({user}) {

    return (
        <Link href="/user">
            <div className="flex items-center justify-center">
                <div className="text-secondary text-xl font-semibold">{user.name}</div>
                {(user.admin) ? (
                    <MdStar className="text-xl text-secondary pl-1" />
                ) : <></>}
            </div>
        </Link>
    )

}
