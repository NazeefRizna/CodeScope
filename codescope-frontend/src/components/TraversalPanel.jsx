import { useState } from "react";

function TraversalPanel({
  classes,
  onDFS,
  onBFS,
  dfsResult,
  bfsResult,
}) {
  const [className, setClassName] = useState("");

  return (
    <section className="panel">
      <h2>Graph Traversal</h2>

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
          Run DFS
        </button>

        <button
          type="button"
          disabled={!className}
          onClick={() => onBFS(className)}
        >
          Run BFS
        </button>
      </div>

      <TraversalResult title="DFS" result={dfsResult} />
      <TraversalResult title="BFS" result={bfsResult} />
    </section>
  );
}

function TraversalResult({ title, result }) {
  if (!result || result.length === 0) {
    return null;
  }

  return (
    <div className="traversal-result">
      <strong>{title} traversal</strong>

      <div className="traversal-path">
        {result.map((classInfo, index) => (
          <span key={`${title}-${classInfo.fullClassName}`}>
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