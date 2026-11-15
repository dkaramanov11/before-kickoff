const matchStatuses = {
    SCHEDULED: { label: 'Upcoming', appearance: 'upcoming' },
    TIMED: { label: 'Upcoming', appearance: 'upcoming' },
    IN_PLAY: { label: 'Live', appearance: 'live' },
    PAUSED: { label: 'Half-time', appearance: 'live' },
    FINISHED: { label: 'Finished', appearance: 'finished' },
    POSTPONED: { label: 'Postponed', appearance: 'neutral' },
    SUSPENDED: { label: 'Suspended', appearance: 'neutral' },
    CANCELLED: { label: 'Cancelled', appearance: 'neutral' },
    AWARDED: { label: 'Awarded', appearance: 'neutral' },
}

export function getMatchStatus(status) {
    return matchStatuses[status] ?? {
        label: 'Unknown status',
        appearance: 'neutral',
    }
}