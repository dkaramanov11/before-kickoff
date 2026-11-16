import TeamForm from '../teams/TeamForm'
import '../../styles/matches/MatchForm.css'

function MatchForm({ match, history }) {
    return (
        <section
            className="match-form"
            aria-labelledby="match-form-heading"
        >
            <h2 id="match-form-heading">Team form</h2>

            <div className="match-form__teams">
                <div>
                    <h3>
                        {match.homeTeam.shortName ?? match.homeTeam.name}
                    </h3>

                    <TeamForm
                        matches={history.homeTeam.recentMatches}
                        teamId={history.homeTeam.teamId}
                    />
                </div>

                <div>
                    <h3>
                        {match.awayTeam.shortName ?? match.awayTeam.name}
                    </h3>

                    <TeamForm
                        matches={history.awayTeam.recentMatches}
                        teamId={history.awayTeam.teamId}
                    />
                </div>
            </div>
        </section>
    )
}

export default MatchForm