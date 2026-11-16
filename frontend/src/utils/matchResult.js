export function getTeamMatchResult(match, teamId) {
    const isHomeTeam = match.homeTeam.id === teamId
    const isAwayTeam = match.awayTeam.id === teamId

    if (!isHomeTeam && !isAwayTeam) {
        return null
    }

    if (match.homeScore == null || match.awayScore == null) {
        return null
    }

    const teamScore = isHomeTeam
        ? match.homeScore
        : match.awayScore

    const opponentScore = isHomeTeam
        ? match.awayScore
        : match.homeScore

    if (teamScore > opponentScore) {
        return 'W'
    }

    if (teamScore < opponentScore) {
        return 'L'
    }

    return 'D'
}