import { useMatchPrediction } from '../../hooks/useMatchPrediction'
import '../../styles/matches/MatchPrediction.css'

function formatProbability(value) {
    return `${(value * 100).toFixed(1)}%`
}

function MatchPrediction({ match }) {
    const { prediction, isLoading, error, retry } =
        useMatchPrediction(match.id)

    const homeName = match.homeTeam.shortName ?? match.homeTeam.name
    const awayName = match.awayTeam.shortName ?? match.awayTeam.name

    const outcomes = prediction?.status === 'ok'
        ? [
            {
                code: '1',
                label: homeName,
                description: 'Home win',
                probability: prediction.probabilities?.homeWin,
            },
            {
                code: 'X',
                label: 'Draw',
                description: 'Draw',
                probability: prediction.probabilities?.draw,
            },
            {
                code: '2',
                label: awayName,
                description: 'Away win',
                probability: prediction.probabilities?.awayWin,
            },
        ]
        : []

    const hasValidPrediction =
        prediction?.status === 'ok' &&
        outcomes.every(({ probability }) =>
            Number.isFinite(probability) &&
            probability >= 0 &&
            probability <= 1
        ) &&
        outcomes.some(({ code }) => code === prediction.predictedOutcome)

    const isUnavailable =
        prediction?.status === 'prediction_unavailable'

    const displayError =
        error ||
        (prediction?.status === 'ok' && !hasValidPrediction
            ? 'Unable to display prediction. Please try again.'
            : null)

    const likelyOutcome = hasValidPrediction
        ? outcomes.find(({ code }) => code === prediction.predictedOutcome)
        : null

    return (
        <section
            className="match-prediction"
            aria-labelledby="match-prediction-heading"
            aria-busy={isLoading}
        >
            <header className="match-prediction__header">
                <h2 id="match-prediction-heading">Model prediction</h2>

                {hasValidPrediction && (
                    <span className="match-prediction__version">
                        Model {prediction.modelVersion}
                    </span>
                )}
            </header>

            {isLoading ? (
                <p className="match-prediction__message" role="status">
                    Loading prediction...
                </p>
            ) : displayError || isUnavailable ? (
                <div>
                    <p className="match-prediction__message" role="alert">
                        {isUnavailable
                            ? 'Prediction is temporarily unavailable.'
                            : displayError}
                    </p>

                    <button
                        className="match-prediction__retry"
                        type="button"
                        onClick={retry}
                    >
                        Try again
                    </button>
                </div>
            ) : prediction?.status === 'insufficient_history' ? (
                <p className="match-prediction__message">
                    Not enough previous matches to generate a prediction.
                    Each team needs five finished Premier League matches
                    within the previous 365 days.
                </p>
            ) : hasValidPrediction ? (
                <>
                    <p className="match-prediction__summary">
                        Most likely outcome:{' '}
                        <strong>
                            {likelyOutcome.description === 'Draw'
                                ? 'Draw'
                                : `${likelyOutcome.label} win`}
                        </strong>
                    </p>

                    <ul className="match-prediction__outcomes">
                        {outcomes.map((outcome) => {
                            const isSelected =
                                outcome.code === prediction.predictedOutcome

                            return (
                                <li
                                    key={outcome.code}
                                    className={
                                        isSelected
                                            ? 'match-prediction__outcome match-prediction__outcome--selected'
                                            : 'match-prediction__outcome'
                                    }
                                >
                                    <span className="match-prediction__code">
                                        {outcome.code}
                                    </span>

                                    <span className="match-prediction__team">
                                        {outcome.label}
                                    </span>

                                    <span className="match-prediction__label">
                                        {outcome.description}
                                    </span>

                                    <strong className="match-prediction__percentage">
                                        {formatProbability(outcome.probability)}
                                    </strong>

                                    <div
                                        className="match-prediction__track"
                                        aria-hidden="true"
                                    >
                                        <div
                                            className="match-prediction__fill"
                                            style={{
                                                width: `${outcome.probability * 100}%`,
                                            }}
                                        />
                                    </div>
                                </li>
                            )
                        })}
                    </ul>
                </>
            ) : null}

            <p className="match-prediction__note">
                Probabilities are model estimates, not guarantees.
            </p>
        </section>
    )
}

export default MatchPrediction