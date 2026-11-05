import '../../styles/layout/PageHeader.css'

function PageHeader() {
    return (
        <header className="page-header">
            <div className="page-header__league">
                <label htmlFor="league">Competition</label>

                <select id="league" defaultValue="PL">
                    <option value="PL">Premier League</option>
                </select>
            </div>

            <h1>Every match. A clearer picture.</h1>

            <p className="page-header__description">
                Football statistics, predictions and match analysis.
            </p>
        </header>
    )
}

export default PageHeader