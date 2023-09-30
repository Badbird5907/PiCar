import ReactDOM from 'react-dom/client'
import Index from './route'
import './index.css'
import {NextUIProvider} from "@nextui-org/react";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Settings from "./route/settings";

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
          <body className={"min-h-screen bg-background font-sans antialiased"}>
          <main className="dark text-foreground bg-background h-full pt-4">
              <RouterProvider router={router} />
          </main>
          </body>
      </NextUIProvider>
  // </React.StrictMode>,
)
