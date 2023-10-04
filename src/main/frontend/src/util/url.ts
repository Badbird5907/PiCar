const fixUrl = (url: string) => {
    return url.replace(/([^:]\/)\/+/g, "$1");
}
export default fixUrl;