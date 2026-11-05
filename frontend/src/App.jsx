import Sidebar from './components/layout/Sidebar'
import PageHeader from './components/layout/PageHeader'
import './styles/App.css'

function App() {
    return (
        <div className="app-layout">
            <Sidebar />

            <main className="app">
                <PageHeader />

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