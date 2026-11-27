import { Link, useParams } from 'react-router-dom'
import { useTeamDetails } from '../hooks/useTeamDetails'
import TeamCrest from '../components/teams/TeamCrest'
import '../styles/teams/TeamDetails.css'
import TeamMatches from '../components/teams/TeamMatches'
import TeamStandings from '../components/teams/TeamStandings'

function TeamDetailsContent({ id }) {
    const { team, isLoading, error, retry } = useTeamDetails(id)

    return (
        <section
            className="team-details"
            aria-labelledby="team-details-heading"
            aria-busy={isLoading}
        >
            <Link className="team-details__back" to="/">
                ← Back to matches
            </Link>

            {isLoading ? (
                <>
                    <h1 id="team-details-heading">Team details</h1>

                    <p className="team-details__message" role="status">
                        Loading team details...
                    </p>
                </>
            ) : error ? (
                <>
                    <h1 id="team-details-heading">Team details</h1>

                    <div className="team-details__panel">
                        <p className="team-details__message" role="alert">
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
                </>
            ) : team ? (
                <>
                    <header className="team-details__header">
                        <TeamCrest url={team.crestUrl} />

                        <div className="team-details__identity">
                            <h1 id="team-details-heading">
                                {team.shortName ?? team.name}
                            </h1>

                            {team.shortName &&
                                team.shortName !== team.name && (
                                    <p>{team.name}</p>
                                )}
                        </div>
                    </header>

                    <TeamStandings teamId={team.id} />

                    <TeamMatches key={team.id} teamId={team.id} />
                </>
            ) : null}
        </section>
    )
}

function TeamDetailsPage() {
    const { id } = useParams()

    return <TeamDetailsContent key={id} id={id} />
}

export default TeamDetailsPage