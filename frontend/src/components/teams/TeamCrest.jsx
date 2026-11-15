import { useState } from 'react'
import '../../styles/teams/TeamCrest.css'

function TeamCrest({ url }) {
    const [failedUrl, setFailedUrl] = useState(null)

    const canDisplayImage = Boolean(url) && failedUrl !== url

    return (
        <span className="team-crest" aria-hidden="true">
            {canDisplayImage ? (
                <img
                    src={url}
                    alt=""
                    loading="lazy"
                    onError={() => setFailedUrl(url)}
                />
            ) : (
                <span className="team-crest__placeholder">—</span>
            )}
        </span>
    )
}

export default TeamCrest