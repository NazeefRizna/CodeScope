import { useState } from "react";

function ProjectScanner({ onScan }) {
  const [projectPath, setProjectPath] = useState("");
  const [scanning, setScanning] = useState(false);

  async function handleScan() {
    const trimmedPath = projectPath.trim();

    if (!trimmedPath) {
      return;
    }

    try {
      setScanning(true);
      await onScan(trimmedPath);
    } finally {
      setScanning(false);
    }
  }

  return (
    <section className="panel">
      <h2>Scan Java Project</h2>

      <p className="muted">
        Paste the full path of the Java project folder.
      </p>

      <div className="search-row">
        <input
          type="text"
          value={projectPath}
          onChange={(event) =>
            setProjectPath(event.target.value)
          }
          onKeyDown={(event) => {
            if (event.key === "Enter") {
              handleScan();
            }
          }}
          placeholder="C:/Users/nazee/Desktop/CodeScope 2"
        />

        <button
          type="button"
          className="primary-button"
          disabled={!projectPath.trim() || scanning}
          onClick={handleScan}
        >
          {scanning ? "Scanning..." : "Scan Project"}
        </button>
      </div>
    </section>
  );
}

export default ProjectScanner;