const APP_TIME_ZONE = 'Europe/Skopje'

export function getDateInSkopje(value) {
    const parts = new Intl.DateTimeFormat('en-GB', {
        timeZone: APP_TIME_ZONE,
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
    }).formatToParts(new Date(value))

    const values = Object.fromEntries(
        parts.map(({ type, value }) => [type, value]),
    )

    return `${values.year}-${values.month}-${values.day}`
}

export function getTodayDate() {
    return getDateInSkopje(new Date())
}

function parseCalendarDate(value) {
    const [year, month, day] = value.split('-').map(Number)

    return new Date(Date.UTC(year, month - 1, day))
}

export function addDays(value, amount) {
    const date = parseCalendarDate(value)

    date.setUTCDate(date.getUTCDate() + amount)

    return date.toISOString().slice(0, 10)
}

export function getWeekStart(value) {
    const date = parseCalendarDate(value)
    const weekday = date.getUTCDay()
    const daysFromMonday = (weekday + 6) % 7

    return addDays(value, -daysFromMonday)
}

export function getWeekDays(weekStart) {
    return Array.from({ length: 7 }, (_, index) =>
        addDays(weekStart, index),
    )
}

export function formatCalendarDate(value, options) {
    return new Intl.DateTimeFormat('en-GB', {
        ...options,
        timeZone: 'UTC',
    }).format(parseCalendarDate(value))
}