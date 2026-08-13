function ClassDetails({
  classInfo,
  onBack,
  onForward,
  onClearHistory,
}) {
  if (!classInfo) {
    return (
      <section className="panel">
        <div className="class-details-toolbar">
          <div className="button-row">
            <button
              type="button"
              onClick={onBack}
            >
              ← Back
            </button>

            <button
              type="button"
              onClick={onForward}
            >
              Forward →
            </button>

            <button
              type="button"
              className="danger-button"
              onClick={onClearHistory}
            >
              Clear
            </button>
          </div>
        </div>

        <h2>Class Details</h2>

        <p className="muted">
          Select or search for a class.
        </p>
      </section>
    );
  }

  return (
    <section className="panel">

      <div className="class-details-toolbar">

        <div className="button-row">
          <button
            type="button"
            onClick={onBack}
          >
            ←
          </button>

          <button
            type="button"
            onClick={onForward}
          >
            →
          </button>

          <button
            type="button"
            className="danger-button"
            onClick={onClearHistory}
          >
            Clear
          </button>
        </div>
      
      </div>

      <h2>Class Details</h2>

      <div className="detail-header">

        <div>
          <h3>{classInfo.className}</h3>

          <p>
            {classInfo.fullClassName}
          </p>
        </div>

        <div className="large-risk">
          {classInfo.componentRiskIndex}

          <small>CRI</small>
        </div>

      </div>

      <div className="details-grid">

        <div>
          <span>Package</span>

          <strong>
            {classInfo.packageName || "Default package"}
          </strong>
        </div>

        <div>
          <span>Incoming dependencies</span>

          <strong>
            {classInfo.incomingDependencies}
          </strong>
        </div>

        <div>
          <span>Outgoing dependencies</span>

          <strong>
            {classInfo.outgoingDependencies}
          </strong>
        </div>

        <div className="full-width">
          <span>File path</span>

          <strong>
            {classInfo.filePath}
          </strong>
        </div>

      </div>

    </section>
  );
}

export default ClassDetails;