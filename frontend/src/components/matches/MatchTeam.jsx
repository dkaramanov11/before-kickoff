import { Link } from 'react-router-dom'
import TeamCrest from '../teams/TeamCrest'
import '../../styles/matches/MatchTeam.css'

function MatchTeam({ team, isAway = false }) {
    const teamName = team.shortName ?? team.name

    const className = isAway
        ? 'match-team match-team--away'
        : 'match-team'

    return (
        <Link
            className={className}
            to={`/teams/${team.id}`}
        >
            <TeamCrest url={team.crestUrl} />
            <span className="match-team__name">{teamName}</span>
        </Link>
    )
}

export default MatchTeam