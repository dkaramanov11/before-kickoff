import Sidebar from './components/layout/Sidebar'
import PageHeader from './components/layout/PageHeader'
import MatchesList from './components/matches/MatchesList'
import './styles/App.css'

function App() {
    return (
        <div className="app-layout">
            <Sidebar />

            <main className="app">
                <PageHeader />
                <MatchesList />
            </main>
        </div>
    )
}

export default App