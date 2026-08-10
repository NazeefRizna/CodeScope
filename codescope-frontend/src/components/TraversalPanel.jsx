import { useState } from "react";

function TraversalPanel({
  classes,
  onDFS,
  dfsResult,
}) {
  const [className, setClassName] = useState("");

  return (
    <section className="panel">
      <h2>Dependency Tracing</h2>

      <p className="muted">
        Trace all dependencies connected from a selected class.
      </p>

      <select
        value={className}
        onChange={(event) =>
          setClassName(event.target.value)
        }
      >
        <option value="">Select starting class</option>

        {classes.map((classInfo) => (
          <option
            value={classInfo.className}
            key={classInfo.fullClassName}
          >
            {classInfo.className}
          </option>
        ))}
      </select>

      <div className="button-row">
        <button
          type="button"
          disabled={!className}
          onClick={() => onDFS(className)}
        >
          Trace Dependencies
        </button>
      </div>

      <TraversalResult result={dfsResult} />
    </section>
  );
}

function TraversalResult({ result }) {
  if (!result || result.length === 0) {
    return null;
  }

  return (
    <div className="traversal-result">
      <h3>Dependency Trace</h3>

      <div className="traversal-path">
        {result.map((classInfo, index) => (
          <span key={classInfo.fullClassName}>
            {classInfo.className}

            {index < result.length - 1 && (
              <i>→</i>
            )}
          </span>
        ))}
      </div>
    </div>
  );
}

export default TraversalPanel;