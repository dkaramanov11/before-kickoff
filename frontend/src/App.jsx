import Sidebar from './components/layout/Sidebar'
import PageHeader from './components/layout/PageHeader'
import MatchesList from './components/matches/MatchesList'
import TeamsList from './components/teams/TeamsList'
import './styles/App.css'

function App() {
    return (
        <div className="app-layout">
            <Sidebar />

            <main className="app">
                <PageHeader />
                <div className="app__content">
                    <MatchesList />
                    <TeamsList />
                </div>
            </main>
        </div>
    )
}

export default App