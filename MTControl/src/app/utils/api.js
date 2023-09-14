import { toast } from "react-toastify"

export const BACKEND = "http://localhost:8080"

export async function request(path, body, showError) {

    try {

        console.info("Request to " + BACKEND + path)

        const response = await fetch(BACKEND + path, {
            method: 'POST',
            body: JSON.stringify(body),
            headers: {
                "Content-Type": 'application/json'
            },
            credentials: "include"
        })

        if (response.status != 200) {
            throw Error("API request failed (Status " + response.status + "): " + response.statusText)
        }
        
        const data = await response.json()

        return data

    } catch (e) {

        console.error("API request failed: " + e)
        if (showError) toast.error("API request failed: " + e)
        return null

    }

}

export function getTitle() {
    return "MineTower"
}

export async function getUser(router, setUser) {

    console.info("Get user")

    const data = await request("/user", {}, true)
    if (data == null) return

    if (data.error != "success") {
        toast.info("You need to login!")
        console.info("Redirect to login")
        router.push("/user/login")
        return
    }

    console.info("User '" + data.username + "' (admin: " + data.admin + ")")
    
    setUser({
        name: data.username,
        admin: data.admin
    })

}
