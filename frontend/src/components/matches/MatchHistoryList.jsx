import MatchCard from './MatchCard'

function MatchHistoryList({ matches, emptyMessage }) {
    if (matches.length === 0) {
        return (
            <p className="match-history__empty">
                {emptyMessage}
            </p>
        )
    }

    return (
        <ul className="match-history__list">
            {matches.map((match) => (
                <li key={match.id}>
                    <MatchCard match={match} />
                </li>
            ))}
        </ul>
    )
}

export default MatchHistoryList