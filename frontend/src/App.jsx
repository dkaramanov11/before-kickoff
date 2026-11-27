import { BrowserRouter, Link, Route, Routes } from 'react-router-dom'
import Sidebar from './components/layout/Sidebar'
import MatchesPage from './pages/MatchesPage'
import './styles/App.css'
import MatchDetailsPage from './pages/MatchDetailsPage'
import TeamDetailsPage from './pages/TeamDetailsPage'

function App() {
    return (
        <BrowserRouter>
            <div className="app-layout">
                <Sidebar />

                <main className="app">
                    <Routes>
                        <Route path="/" element={<MatchesPage />} />
                        <Route path="/matches/:id" element={<MatchDetailsPage />} />
                        <Route path="/teams/:id" element={<TeamDetailsPage />} />
                        <Route
                            path="*"
                            element={
                                <section>
                                    <h1>Page not found</h1>
                                    <Link to="/">Back to matches</Link>
                                </section>
                            }
                        />
                    </Routes>
                </main>
            </div>
        </BrowserRouter>
    )
}

export default App