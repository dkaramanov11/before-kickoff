const matchDateTimeFormatter = new Intl.DateTimeFormat('en-GB', {
    timeZone: 'Europe/Skopje',
    day: '2-digit',
    month: 'short',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
})

export function formatMatchDateTime(value) {
    if (!value) {
        return 'Date to be confirmed'
    }

    const date = new Date(value)

    if (Number.isNaN(date.getTime())) {
        return 'Date unavailable'
    }

    return matchDateTimeFormatter.format(date)
}