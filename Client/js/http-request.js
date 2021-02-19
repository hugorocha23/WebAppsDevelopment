export function getRequest (url, headers) {
    return new Request(url, {
        headers,
        method: 'GET',
    })
}

export function postRequest(url, body, headers) {
    return new Request(url, {
        headers,
        method: 'POST',
        body: JSON.stringify(body)
    })
}

export function putRequest(url, body, headers) {
    return new Request(url, {
        headers,
        method: 'PUT',
        body: JSON.stringify(body)
    })
}

export function deleteRequest(url, headers){
    return new Request(url, {
        headers,
        method: 'DELETE',
    })
}