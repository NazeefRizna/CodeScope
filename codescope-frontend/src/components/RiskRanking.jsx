function RiskRanking({
  highestRisk,
  topRiskClasses,
  onSelectClass,
}) {
  return (
    <section className="panel">
      <h2>Component Risk Ranking</h2>

      {highestRisk ? (
        <button
          type="button"
          className="highest-risk-card"
          onClick={() =>
            onSelectClass(highestRisk.classInfo)
          }
        >
          <span>Highest risk</span>
          <strong>
            {highestRisk.classInfo.className}
          </strong>
          <b>{highestRisk.riskScore}</b>
        </button>
      ) : (
        <p className="muted">No risk data available.</p>
      )}

      <div className="ranking-list">
        {topRiskClasses.map((item, index) => (
          <button
            type="button"
            className="ranking-item"
            key={item.classInfo.fullClassName}
            onClick={() =>
              onSelectClass(item.classInfo)
            }
          >
            <span className="rank-number">
              {index + 1}
            </span>

            <div>
              <strong>
                {item.classInfo.className}
              </strong>

              <small>
                {item.classInfo.packageName}
              </small>
            </div>

            <b>{item.riskScore}</b>
          </button>
        ))}
      </div>
    </section>
  );
}

export default RiskRanking;