import PageHeader from '../components/layout/PageHeader'
import MatchesList from '../components/matches/MatchesList'
import StandingsTable from '../components/standings/StandingsTable'
import { useInitialMatchDate } from '../hooks/useInitialMatchDate'
import { getTodayDate } from '../utils/calendar'

function MatchesPage() {
    const { initialDate, isLoading, error } = useInitialMatchDate()

    return (
        <>
            <PageHeader />

            <div className="app__content">
                {isLoading ? (
                    <section className="matches">
                        <h2>Matches</h2>
                        <p className="matches__message" role="status">
                            Finding the next scheduled match...
                        </p>
                    </section>
                ) : (
                    <div>
                        {error && (
                            <p role="status">
                                {error} Showing today instead.
                            </p>
                        )}

                        <MatchesList initialDate={initialDate || getTodayDate()} />
                    </div>
                )}

                <StandingsTable />
            </div>
        </>
    )
}

export default MatchesPage