import { useTeamMatches } from '../../hooks/useTeamMatches'
import TeamForm from './TeamForm'
import MatchCard from '../matches/MatchCard'
import '../../styles/teams/TeamMatches.css'

function MatchesSection({ title, matches, emptyMessage }) {
    return (
        <section className="team-matches__section">
            <h2>{title}</h2>

            {matches.length === 0 ? (
                <p className="team-matches__message">
                    {emptyMessage}
                </p>
            ) : (
                <ul className="team-matches__list">
                    {matches.map((match) => (
                        <li key={match.id}>
                            <MatchCard match={match} />
                        </li>
                    ))}
                </ul>
            )}
        </section>
    )
}

function TeamMatches({ teamId }) {
    const { data, isLoading, error, retry } = useTeamMatches(teamId)

    if (isLoading) {
        return (
            <div className="team-matches__panel" aria-busy="true">
                <p className="team-matches__message" role="status">
                    Loading team matches...
                </p>
            </div>
        )
    }

    if (error) {
        return (
            <div className="team-matches__panel">
                <p className="team-matches__message" role="alert">
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
        )
    }

    if (!data) return null

    return (
        <div className="team-matches">
            <section className="team-matches__panel">
                <h2>Recent form</h2>

                <TeamForm
                    matches={data.recentMatches}
                    teamId={data.teamId}
                />
            </section>

            <div className="team-matches__columns">
                <MatchesSection
                    title="Last 5 matches"
                    matches={data.recentMatches}
                    emptyMessage="No recent matches available."
                />

                <MatchesSection
                    title="Next 5 matches"
                    matches={data.upcomingMatches}
                    emptyMessage="No upcoming matches scheduled."
                />
            </div>
        </div>
    )
}

export default TeamMatches