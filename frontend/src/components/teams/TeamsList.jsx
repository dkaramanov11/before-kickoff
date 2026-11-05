import { useTeams } from '../../hooks/useTeams'
import '../../styles/teams/TeamsList.css'

function TeamsList() {
    const { teams, isLoading, error } = useTeams()

    return (
        <section
            className="teams"
            aria-labelledby="teams-heading"
            aria-busy={isLoading}
        >
            <header className="teams__header">
                <h2 id="teams-heading">League teams</h2>
                <p>Premier League</p>
            </header>

            {isLoading ? (
                <p className="teams__message" role="status">
                    Loading teams...
                </p>
            ) : error ? (
                <p className="teams__message" role="alert">
                    Unable to load teams. Please try again later.
                </p>
            ) : teams.length === 0 ? (
                <p className="teams__message">No teams found.</p>
            ) : (
                <>
                    <p className="teams__note">Listed alphabetically</p>

                    <ul className="teams__list">
                        {teams.map((team) => (
                            <li key={team.id} className="teams__item">
                <span className="teams__crest">
                  {team.crestUrl ? (
                      <img
                          src={team.crestUrl}
                          alt=""
                          loading="lazy"
                          onError={(event) => {
                              event.currentTarget.hidden = true
                          }}
                      />
                  ) : (
                      <span aria-hidden="true">—</span>
                  )}
                </span>

                                <span>{team.shortName || team.name}</span>
                            </li>
                        ))}
                    </ul>
                </>
            )}
        </section>
    )
}

export default TeamsList