function ClassList({
  classes,
  selectedClass,
  onSelectClass,
}) {
  return (
    <section className="panel">
      <div className="section-heading">
        <div>
          <h2>Scanned Classes</h2>
          <p className="muted">
            Classes extracted from the selected project.
          </p>
        </div>

        <strong>{classes.length}</strong>
      </div>

      {classes.length === 0 ? (
        <p className="muted">No classes loaded.</p>
      ) : (
        <div className="class-list">
          {classes.map((classInfo) => {
            const selected =
              selectedClass?.fullClassName ===
              classInfo.fullClassName;

            return (
              <button
                type="button"
                className={`class-item ${
                  selected ? "selected" : ""
                }`}
                key={classInfo.fullClassName}
                onClick={() => onSelectClass(classInfo)}
              >
                <div>
                  <strong>{classInfo.className}</strong>
                  <small>{classInfo.packageName}</small>
                </div>

                <span className="risk-badge">
                  CRI {classInfo.componentRiskIndex}
                </span>
              </button>
            );
          })}
        </div>
      )}
    </section>
  );
}

export default ClassList;