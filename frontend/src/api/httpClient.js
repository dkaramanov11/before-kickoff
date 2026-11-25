export class HttpError extends Error {
    constructor(status, body = null) {
        super(`Request failed with status ${status}`)
        this.name = 'HttpError'
        this.status = status
        this.body = body
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
        let body = null

        try {
            body = await response.json()
        } catch (error) {
            if (signal?.aborted) {
                throw error
            }
        }

        throw new HttpError(response.status, body)
    }

    return response.json()
}