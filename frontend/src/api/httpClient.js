export class HttpError extends Error {
    constructor(status) {
        super(`Request failed with status ${status}`)
        this.name = 'HttpError'
        this.status = status
    }
}

export async function getJson(path, { signal } = {}) {
    const response = await fetch(path, {
        signal,
        headers: {
            Accept: 'application/json',
        },
    })

    if (!response.ok) {
        throw new HttpError(response.status)
    }

    return response.json()
}