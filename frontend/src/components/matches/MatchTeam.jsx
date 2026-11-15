import TeamCrest from '../teams/TeamCrest'
import '../../styles/matches/MatchTeam.css'

function MatchTeam({ team, isAway = false }) {
    const teamName = team.shortName ?? team.name

    const className = isAway
        ? 'match-team match-team--away'
        : 'match-team'

    return (
        <div className={className}>
            <TeamCrest url={team.crestUrl} />
            <span className="match-team__name">{teamName}</span>
        </div>
    )
}

export default MatchTeam