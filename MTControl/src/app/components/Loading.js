import { PulseLoader } from "react-spinners"
import tailwindConfig from "../../../tailwind.config"

export default function Loading() {

    return (
        <div className="h-full flex items-center justify-center">
            <PulseLoader color={tailwindConfig.theme.extend.colors["fg-primary"]} />
        </div>
    )

}
