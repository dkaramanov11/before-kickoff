import PageHeader from '../components/layout/PageHeader'
import MatchesList from '../components/matches/MatchesList'
import TeamsList from '../components/teams/TeamsList'

function MatchesPage() {
    return (
        <>
            <PageHeader />

            <div className="app__content">
                <MatchesList />
                <TeamsList />
            </div>
        </>
    )
}

export default MatchesPage