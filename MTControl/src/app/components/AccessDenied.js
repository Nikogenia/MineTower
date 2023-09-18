export default function AccessDenied() {

    return (
        <div className="h-full flex items-center justify-center">
            <div className="bg-bg-primary p-4 rounded-lg flex flex-col items-center justify-center w-[30rem] max-w-[90%] h-[16rem] max-h-[90%]">
                <div className="text-4xl text-center mb-8">Access denied!</div>
                <div className="text-xl text-center">You are not allowed to access this page!</div>
            </div>
        </div>
    )

}
