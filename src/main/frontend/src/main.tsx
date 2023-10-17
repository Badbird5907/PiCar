import ReactDOM from 'react-dom/client'
import Index from './route'
import './index.css'
import {NextUIProvider} from "@nextui-org/react";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Settings from "./route/settings";
import {DynamicModalProvider} from "@/components/dynamic-modal.tsx";
import {SWRConfig} from "swr";
import apiClient from "@/util/api-client.ts";

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
                return apiClient.get(path).then(res => res.data);
            }
        }}>
            <div className={"body-div min-h-screen bg-background font-sans antialiased"}>
            <main className="dark text-foreground bg-background h-full pt-4">
                <DynamicModalProvider>
                    <RouterProvider router={router}/>
                </DynamicModalProvider>
            </main>
            </div>
        </SWRConfig>
    </NextUIProvider>
    // </React.StrictMode>,
)
