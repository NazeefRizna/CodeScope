import { useState } from "react";

function ShortestPathPanel({
  classes,
  onFindShortestPath,
  shortestPath,
}) {
  const [startClass, setStartClass] = useState("");
  const [targetClass, setTargetClass] = useState("");

  function handleFindPath() {
    if (!startClass || !targetClass) {
      return;
    }

    onFindShortestPath(startClass, targetClass);
  }

  return (
    <section className="panel">
      <h2>Shortest Dependency Path</h2>

      <p className="muted">
        Find the shortest dependency connection between two classes.
      </p>

      <div className="shortest-path-inputs">

        <select
          value={startClass}
          onChange={(event) =>
            setStartClass(event.target.value)
          }
        >
          <option value="">
            Select starting class
          </option>

          {classes.map((classInfo) => (
            <option
              key={`start-${classInfo.fullClassName}`}
              value={classInfo.className}
            >
              {classInfo.className}
            </option>
          ))}
        </select>

        <select
          value={targetClass}
          onChange={(event) =>
            setTargetClass(event.target.value)
          }
        >
          <option value="">
            Select target class
          </option>

          {classes.map((classInfo) => (
            <option
              key={`target-${classInfo.fullClassName}`}
              value={classInfo.className}
            >
              {classInfo.className}
            </option>
          ))}
        </select>

      </div>

      <div className="button-row">
        <button
          type="button"
          disabled={
            !startClass ||
            !targetClass ||
            startClass === targetClass
          }
          onClick={handleFindPath}
        >
          Find Shortest Path
        </button>
      </div>

      <ShortestPathResult
        result={shortestPath}
      />
    </section>
  );
}

function ShortestPathResult({ result }) {

  if (!result || result.length === 0) {
    return null;
  }

  return (
    <div className="traversal-result">

      <h3>Shortest Dependency Path</h3>

      <div className="traversal-path">

        {result.map((classInfo, index) => (
          <span
            key={`path-${classInfo.fullClassName}`}
          >
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

export default ShortestPathPanel;