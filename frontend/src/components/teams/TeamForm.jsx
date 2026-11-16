import { getTeamMatchResult } from '../../utils/matchResult'
import '../../styles/teams/TeamForm.css'

const resultLabels = {
    W: 'Win',
    D: 'Draw',
    L: 'Loss',
}

function TeamForm({ matches, teamId }) {
    const chronologicalMatches = [...matches].reverse()

    if (chronologicalMatches.length === 0) {
        return (
            <p className="team-form__empty">
                No recent matches available.
            </p>
        )
    }

    return (
        <div className="team-form">
            <ul className="team-form__list" aria-label="Recent match form">
                {chronologicalMatches.map((match) => {
                    const result = getTeamMatchResult(match, teamId)

                    if (!result) return null

                    const homeName =
                        match.homeTeam.shortName ?? match.homeTeam.name

                    const awayName =
                        match.awayTeam.shortName ?? match.awayTeam.name

                    const description =
                        `${resultLabels[result]}: ${homeName} ` +
                        `${match.homeScore}–${match.awayScore} ${awayName}`

                    return (
                        <li
                            key={match.id}
                            className={`team-form__badge team-form__badge--${result.toLowerCase()}`}
                            aria-label={description}
                            title={description}
                        >
                            <span aria-hidden="true">{result}</span>
                        </li>
                    )
                })}
            </ul>

            <p className="team-form__hint">
                Oldest to newest →
            </p>
        </div>
    )
}

export default TeamForm