import '../../styles/layout/Sidebar.css'
import { Link } from 'react-router-dom'

function Sidebar() {
    return (
        <aside className="sidebar">
            <div className="sidebar__brand">
        <span className="sidebar__monogram" aria-hidden="true">
          BK
        </span>

                <span className="sidebar__name">
          Before
          <br />
          Kickoff
        </span>
            </div>

            <nav className="sidebar__navigation" aria-label="Main navigation">
                <Link
                    className="sidebar__link sidebar__link--active"
                    to="/"
                >
                    Matches
                </Link>

                <span className="sidebar__link sidebar__link--disabled">
          Predictions
          <span className="sidebar__soon">Soon</span>
        </span>

                <span className="sidebar__link sidebar__link--disabled">
          Model performance
          <span className="sidebar__soon">Soon</span>
        </span>
            </nav>

            <div className="sidebar__footer">
                <p>Premier League</p>
                <span>Football match analysis</span>
            </div>
        </aside>
    )
}

export default Sidebar