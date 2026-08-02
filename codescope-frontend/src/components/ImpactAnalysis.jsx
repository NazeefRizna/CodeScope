import { useState } from "react";

function ImpactAnalysis({
  classes = [],
  results = null,
  onAnalyze,
}) {
  const [className, setClassName] = useState("");

  const safeResults = Array.isArray(results)
    ? results
    : [];

  return (
    <section className="panel">
      <h2>Impact Analysis</h2>

      <div className="search-row">
        <select
          value={className}
          onChange={(event) =>
            setClassName(event.target.value)
          }
        >
          <option value="">
            Select changed class
          </option>

          {classes.map((classInfo) => (
            <option
              value={classInfo.className}
              key={classInfo.fullClassName}
            >
              {classInfo.className}
            </option>
          ))}
        </select>

        <button
          type="button"
          className="primary-button"
          disabled={!className}
          onClick={() => onAnalyze(className)}
        >
          Analyze impact
        </button>
      </div>

      {results !== null && (
        <div className="impact-results">
          {safeResults.length === 0 ? (
            <p className="muted">
              No other classes are affected.
            </p>
          ) : (
            safeResults.map((result, index) => (
              <article
                className="impact-item"
                key={
                  result.fullClassName ||
                  `${result.className}-${index}`
                }
              >
                <div>
                  <strong>{result.className}</strong>
                  <small>{result.packageName}</small>
                </div>

                <div>
                  <span>{result.impactType}</span>
                  <b>
                    Level {result.impactLevel}
                  </b>
                </div>
              </article>
            ))
          )}
        </div>
      )}
    </section>
  );
}

export default ImpactAnalysis;