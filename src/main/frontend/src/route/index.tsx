function Index() {
    return (
        <div>
            <h1 className={
                "text-4xl text-center text-foreground dark:text-foreground-dark font-bold"
            }>PiCar</h1>
            <a href={"/settings"} className={
                "text-2xl text-center text-foreground dark:text-foreground-dark font-bold"
            }>Settings</a>
        </div>
    )
}

export default Index
