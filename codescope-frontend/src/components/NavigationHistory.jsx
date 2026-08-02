function NavigationHistory({
  currentClass,
  onBack,
  onForward,
  onClear,
}) {
  return (
    <section className="panel">
      <h2>Navigation History</h2>

      <div className="current-history">
        <span>Current class</span>
        <strong>
          {currentClass || "No class selected"}
        </strong>
      </div>

      <div className="button-row">
        <button type="button" onClick={onBack}>
          ← Back
        </button>

        <button type="button" onClick={onForward}>
          Forward →
        </button>

        <button
          type="button"
          className="danger-button"
          onClick={onClear}
        >
          Clear
        </button>
      </div>
    </section>
  );
}

export default NavigationHistory;
