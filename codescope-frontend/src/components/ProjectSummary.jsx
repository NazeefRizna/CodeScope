function ProjectSummary({ classes }) {
  return (
    <section className="panel">
      <h2>Scanned Classes</h2>

      <p>Total classes: {classes.length}</p>

      {classes.length === 0 ? (
        <p>No project has been scanned.</p>
      ) : (
        <div className="class-grid">
          {classes.map((classInfo) => (
            <article
              className="class-card"
              key={classInfo.fullClassName}
            >
              <h3>{classInfo.className}</h3>

              <p>
                <strong>Package:</strong>{" "}
                {classInfo.packageName || "Default package"}
              </p>

              <p>
                <strong>Incoming:</strong>{" "}
                {classInfo.incomingDependencies}
              </p>

              <p>
                <strong>Outgoing:</strong>{" "}
                {classInfo.outgoingDependencies}
              </p>

              <p>
                <strong>CRI:</strong>{" "}
                {classInfo.componentRiskIndex}
              </p>
            </article>
          ))}
        </div>
      )}
    </section>
  );
}

export default ProjectSummary;