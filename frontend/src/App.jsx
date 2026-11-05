import Sidebar from './components/layout/Sidebar'
import './styles/App.css'

function App() {
    return (
        <div className="app-layout">
            <Sidebar />

            <main className="app">
                <header className="app__header">
                    <p className="app__competition">Premier League</p>
                    <h1>Every match. A clearer picture.</h1>
                    <p>Football statistics, predictions and match analysis.</p>
                </header>

                <section
                    id="matches"
                    className="app__section"
                    aria-labelledby="matches-heading"
                >
                    <h2 id="matches-heading">Matches</h2>
                    <p>The match list will appear here.</p>
                </section>
            </main>
        </div>
    )
}

export default App