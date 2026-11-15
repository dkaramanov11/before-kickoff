import { getMatchStatus } from '../../utils/matchStatus'
import '../../styles/matches/MatchStatus.css'

function MatchStatus({ status }) {
    const { label, appearance } = getMatchStatus(status)

    return (
        <span className={`match-status match-status--${appearance}`}>
            {label}
        </span>
    )
}

export default MatchStatus