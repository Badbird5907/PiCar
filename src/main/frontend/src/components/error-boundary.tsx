import {ErrorBoundary as ReactErrorBoundary} from "react-error-boundary";
import {Component, FunctionComponent, ReactElement} from "react";

type ReactThing = ReactElement<unknown, string | FunctionComponent | typeof Component> | null;
type ErrorBoundaryProps = {
    children: React.ReactNode;
    fallback?: ReactThing;
}
const ErrorBoundary = ({
    fallback = <>
        <div>
            <span className={"text-4xl text-white"}>
                Something went wrong. Please refresh the page.
            </span>
        </div>
    </>,
    children
}: ErrorBoundaryProps) => {
    return (
        <ReactErrorBoundary fallback={fallback}>
            {children ?? null}
        </ReactErrorBoundary>
    );
};

export default ErrorBoundary;