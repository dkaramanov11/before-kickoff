import './styles/App.css'

function App() {
  return (
      <main className="app">
        <header className="app__header">
          <p className="app__competition">Premier League</p>
          <h1>Before Kickoff</h1>
          <p>Football statistics, predictions and match analysis.</p>
        </header>

        <section className="app__section" aria-labelledby="matches-heading">
          <h2 id="matches-heading">Matches</h2>
          <p>The match list will appear here.</p>
        </section>
      </main>
  )
}

export default App