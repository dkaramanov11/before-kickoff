import { useState } from 'react'
import { useStandings } from '../../hooks/useStandings'
import { formatMatchDateTime } from '../../utils/dateTime'
import '../../styles/standings/StandingsTable.css'

function TeamCrest({ url }) {
    const [failedUrl, setFailedUrl] = useState(null)

    return (
        <span className="standings__crest" aria-hidden="true">
            {url && failedUrl !== url ? (
                <img
                    src={url}
                    alt=""
                    loading="lazy"
                    onError={() => setFailedUrl(url)}
                />
            ) : (
                <span className="standings__crest-placeholder">—</span>
            )}
        </span>
    )
}

function StandingsTable({
                            homeTeamId,
                            awayTeamId,
                            title = 'League table',
                        }) {
    const {
        data,
        isLoading,
        error,
        isUnavailable,
        retry,
    } = useStandings()

    const hasStandings = data && data.standings.length > 0

    return (
        <section
            className="standings"
            aria-labelledby="standings-heading"
            aria-busy={isLoading}
        >
            <header className="standings__header">
                <h2 id="standings-heading">{title}</h2>

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
                <p className="standings__message" role="status">
                    Loading standings...
                </p>
            ) : error ? (
                <div className="standings__message">
                    <p role="alert">{error}</p>

                    <button
                        type="button"
                        className="standings__retry"
                        onClick={retry}
                    >
                        Try again
                    </button>
                </div>
            ) : isUnavailable || !hasStandings ? (
                <p className="standings__message">
                    Standings are not available yet.
                </p>
            ) : (
                <>
                    <div className="standings__table-wrapper">
                        <table className="standings__table">
                            <caption className="standings__caption">
                                Latest saved {data.competitionName} standings
                            </caption>

                            <thead>
                            <tr>
                                <th scope="col" aria-label="Position">
                                    #
                                </th>
                                <th
                                    scope="col"
                                    className="standings__team-heading"
                                >
                                    Team
                                </th>
                                <th scope="col" aria-label="Played games">
                                    P
                                </th>
                                <th
                                    scope="col"
                                    aria-label="Goals for and against"
                                >
                                    G
                                </th>
                                <th scope="col" aria-label="Points">
                                    Pts
                                </th>
                            </tr>
                            </thead>

                            <tbody>
                            {data.standings.map((entry) => {
                                const role =
                                    entry.team.id === homeTeamId
                                        ? 'Home'
                                        : entry.team.id === awayTeamId
                                            ? 'Away'
                                            : null

                                return (
                                    <tr
                                        key={entry.team.id}
                                        className={
                                            role
                                                ? 'standings__row--highlighted'
                                                : undefined
                                        }
                                    >
                                        <td>{entry.position}</td>

                                        <th
                                            scope="row"
                                            className="standings__team-cell"
                                        >
                                            <div className="standings__team">
                                                <TeamCrest
                                                    url={entry.team.crestUrl}
                                                />

                                                <div className="standings__team-info">
                                                        <span>
                                                            {entry.team.shortName ??
                                                                entry.team.name}
                                                        </span>

                                                    {role && (
                                                        <span className="standings__team-role">
                                                                {role}
                                                            </span>
                                                    )}
                                                </div>
                                            </div>
                                        </th>

                                        <td>{entry.playedGames}</td>

                                        <td className="standings__goals">
                                            {entry.goalsFor}:{entry.goalsAgainst}
                                        </td>

                                        <td className="standings__points">
                                            {entry.points}
                                        </td>
                                    </tr>
                                )
                            })}
                            </tbody>
                        </table>
                    </div>

                    <p className="standings__updated">
                        Last imported:{' '}
                        <time dateTime={data.lastSyncedAt}>
                            {formatMatchDateTime(data.lastSyncedAt)}
                        </time>
                    </p>
                </>
            )}
        </section>
    )
}

export default StandingsTable