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

export async function checkUser(router, setUser) {

    console.info("Get user")
  
    const data = await request("/user", {}, false)
    if (data == null) return
    if (data.error != "success") return
  
    console.info("User '" + data.username + "' (admin: " + data.admin + ")")
    
    setUser({
        name: data.username,
        admin: data.admin
    })
  
    router.push("/")
  
  }
  
export async function login(router, username, password) {
  
    console.info("Log in as '" + username + "'")
  
    const data = await request("/user/login", {
        username: username.toLowerCase(),
        password: password
    }, true)
    if (data == null) return
  
    if (data.error == "success") {
        console.info("Log in successful")
        console.info("Redirect to home")
        router.push("/")
        return
    }
  
    console.log("Login failed: " + data.error)
    toast.error("Login failed (" + data.error + "): " + data.message)
  
}

export async function logout(router, setUser) {

    console.info("Logout")
  
    setUser({
      name: "",
      admin: false
    })
  
    const data = await request("/user/logout", {}, true)
    if (data == null) return
  
    if (data.error == "success") {
        console.info("Log out successful")
        console.info("Redirect to login")
        router.push("/user/login")
        return
    }
  
    console.log("Logout failed: " + data.error)
    toast.error("Logout failed (" + data.error + "): " + data.message)
    router.push("/user/login")
  
}

export async function changePassword(username, password) {

    console.info("Change password for '" + username + "'")
  
    const data = await request("/user/password", {
        password: password
    }, true)
    if (data == null) return
  
    if (data.error == "success") {
        console.info("Password change successful")
        toast.success("Password changed successfully!")
        return
    }
  
    console.log("Password change failed: " + data.error)
    toast.error("Password change failed (" + data.error + "): " + data.message)
  
}

export async function getUsers(setUsers) {

    console.info("Get users")
  
    const data = await request("/user/list", {}, true)
    if (data == null) return
  
    if (data.error == "success") {
        console.info("Get users successful")
        setUsers(data.users)
        return
    }
  
    console.log("Get users failed: " + data.error)
    toast.error("Getting users failed (" + data.error + "): " + data.message)
  
}
  
export async function unregisterUser(username) {
  
    console.info("Unregister user '" + username + "'")
  
    const data = await request("/user/unregister", {
      username: username
    }, true)
    if (data == null) return
  
    if (data.error == "success") {
        console.info("Unregister successful")
        toast.success("User successfully unregistered!")
        return
    }
  
    console.log("Unregister failed: " + data.error)
    toast.error("Unregister user failed (" + data.error + "): " + data.message)
  
}
  
export async function registerUser(username, password, admin) {
  
    console.info("Register user '" + username + "'")

    const data = await request("/user/register", {
    username: username.toLowerCase(),
    password: password,
    admin: admin
    }, true)
    if (data == null) return

    if (data.error == "success") {
        console.info("Register successful")
        toast.success("User successfully registered!")
        return
    }

    console.log("Register failed: " + data.error)
    toast.error("Register user failed (" + data.error + "): " + data.message)
    
}
