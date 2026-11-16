import { useState } from 'react'
import { useMatchHistory } from '../../hooks/useMatchHistory'
import MatchForm from './MatchForm'
import MatchHistoryList from './MatchHistoryList'
import '../../styles/matches/MatchHistory.css'

function MatchHistory({ match }) {
    const [selectedView, setSelectedView] = useState('home')
    const { history, isLoading, error, retry } = useMatchHistory(match.id)

    const homeName = match.homeTeam.shortName ?? match.homeTeam.name
    const awayName = match.awayTeam.shortName ?? match.awayTeam.name

    const views = [
        {
            id: 'home',
            label: homeName,
            heading: `${homeName} — last 5 matches`,
            matches: history?.homeTeam.recentMatches ?? [],
            emptyMessage: 'No recent matches available for this team.',
        },
        {
            id: 'away',
            label: awayName,
            heading: `${awayName} — last 5 matches`,
            matches: history?.awayTeam.recentMatches ?? [],
            emptyMessage: 'No recent matches available for this team.',
        },
        {
            id: 'head-to-head',
            label: 'Head-to-head',
            heading: 'Last 5 head-to-head matches',
            matches: history?.headToHead ?? [],
            emptyMessage: 'No previous meetings available.',
        },
    ]

    const activeView = views.find((view) => view.id === selectedView)

    return (
        <div className="match-history" aria-busy={isLoading}>
            {isLoading ? (
                <p className="match-history__message" role="status">
                    Loading match history...
                </p>
            ) : error ? (
                <div className="match-history__error">
                    <p className="match-history__message" role="alert">
                        {error}
                    </p>

                    <button
                        className="match-history__retry"
                        type="button"
                        onClick={retry}
                    >
                        Try again
                    </button>
                </div>
            ) : history ? (
                <>
                    <MatchForm match={match} history={history} />

                    <section
                        className="match-history__results"
                        aria-labelledby="match-history-heading"
                    >
                        <h2 id="match-history-heading">Match history</h2>

                        <div
                            className="match-history__switcher"
                            role="group"
                            aria-label="Choose match history"
                        >
                            {views.map((view) => (
                                <button
                                    key={view.id}
                                    className="match-history__option"
                                    type="button"
                                    aria-pressed={selectedView === view.id}
                                    onClick={() => setSelectedView(view.id)}
                                >
                                    {view.label}
                                </button>
                            ))}
                        </div>

                        <h3 className="match-history__heading">
                            {activeView.heading}
                        </h3>

                        <MatchHistoryList
                            matches={activeView.matches}
                            emptyMessage={activeView.emptyMessage}
                        />
                    </section>
                </>
            ) : null}
        </div>
    )
}

export default MatchHistory