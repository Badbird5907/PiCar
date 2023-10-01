import ReactDOM from 'react-dom/client'
import Index from './route'
import './index.css'
import {NextUIProvider} from "@nextui-org/react";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Settings from "./route/settings";
import {DynamicModalProvider} from "@/components/dynamic-modal.tsx";
import {SWRConfig} from "swr";
import {getSettingValue} from "@/util/setting.ts";
import axios from "axios";

const router = createBrowserRouter([
    {
        path: "/",
        element: <Index/>,
    },
    {
        path: "/settings",
        element: <Settings/>,
    }
]);


ReactDOM.createRoot(document.getElementById('root')!).render(
    // <React.StrictMode>
    <NextUIProvider>
        <SWRConfig value={{
            fetcher: (path) => {
                let backend = getSettingValue("apiUrl");
                // concat backend and path, but remove duplicate slashes
                if (backend.endsWith("/")) {
                    backend = backend.slice(0, -1);
                }
                if (path.startsWith("/")) {
                    path = path.slice(1);
                }
                const url = backend + "/" + path;
                return axios.get(url).then(res => res.data);
            }
        }}>
            <body className={"min-h-screen bg-background font-sans antialiased"}>
            <main className="dark text-foreground bg-background h-full pt-4">
                <DynamicModalProvider>
                    <RouterProvider router={router}/>
                </DynamicModalProvider>
            </main>
            </body>
        </SWRConfig>
    </NextUIProvider>
    // </React.StrictMode>,
)
