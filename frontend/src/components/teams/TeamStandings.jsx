import { useStandings } from '../../hooks/useStandings'
import { formatMatchDateTime } from '../../utils/dateTime'
import '../../styles/teams/TeamStandings.css'

function TeamStandings({ teamId }) {
    const {
        data,
        isLoading,
        error,
        isUnavailable,
        retry,
    } = useStandings()

    const entry = data?.standings.find(
        (standing) => standing.team.id === teamId
    )

    const statistics = entry
        ? [
            { label: 'Position', value: entry.position },
            { label: 'Played', value: entry.playedGames },
            { label: 'Won', value: entry.won },
            { label: 'Drawn', value: entry.draw },
            { label: 'Lost', value: entry.lost },
            {
                label: 'Goals',
                value: `${entry.goalsFor}:${entry.goalsAgainst}`,
            },
            { label: 'Goal difference', value: entry.goalDifference },
            { label: 'Points', value: entry.points },
        ]
        : []

    return (
        <section
            className="team-standings"
            aria-labelledby="team-standings-heading"
            aria-busy={isLoading}
        >
            <header className="team-standings__header">
                <h2 id="team-standings-heading">Current league standing</h2>

                {data && (
                    <p>
                        {data.competitionName}
                        {' · '}
                        {data.seasonStartYear}
                        {'–'}
                        {String(data.seasonStartYear + 1).slice(-2)}
                    </p>
                )}
            </header>

            {isLoading ? (
                <p className="team-standings__message" role="status">
                    Loading league standing...
                </p>
            ) : error ? (
                <div>
                    <p className="team-standings__message" role="alert">
                        {error}
                    </p>

                    <button
                        className="team-details__retry"
                        type="button"
                        onClick={retry}
                    >
                        Try again
                    </button>
                </div>
            ) : isUnavailable || !data ? (
                <p className="team-standings__message">
                    Standings are not available yet.
                </p>
            ) : !entry ? (
                <p className="team-standings__message">
                    No current league standing is available for this team.
                </p>
            ) : (
                <>
                    <dl className="team-standings__statistics">
                        {statistics.map(({ label, value }) => (
                            <div
                                key={label}
                                className="team-standings__stat"
                            >
                                <dt>{label}</dt>
                                <dd>{value}</dd>
                            </div>
                        ))}
                    </dl>

                    <p className="team-standings__updated">
                        Latest saved standings. Last imported:{' '}
                        <time dateTime={data.lastSyncedAt}>
                            {formatMatchDateTime(data.lastSyncedAt)}
                        </time>
                    </p>
                </>
            )}
        </section>
    )
}

export default TeamStandings