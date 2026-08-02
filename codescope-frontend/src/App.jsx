import { useState } from "react";
import { Toaster, toast } from "react-hot-toast";

import ProjectScanner from "./components/ProjectScanner";
import DependencyGraph from "./components/DependencyGraph";
import ClassSearch from "./components/ClassSearch";
import ClassList from "./components/ClassList";
import ClassDetails from "./components/ClassDetails";
import RiskRanking from "./components/RiskRanking";
import TraversalPanel from "./components/TraversalPanel";
import ImpactAnalysis from "./components/ImpactAnalysis";
import NavigationHistory from "./components/NavigationHistory";

import {
  clearHistory,
  getAllClasses,
  getCurrentHistoryClass,
  getGraphData,
  getHighestRiskClass,
  getImpactAnalysis,
  getTopRiskClasses,
  goBack,
  goForward,
  runBFS,
  runDFS,
  scanProject,
  searchClass,
} from "./api/codescopeApi";

import "./App.css";

function App() {
  const [classes, setClasses] = useState([]);
  const [graphData, setGraphData] = useState(null);
  const [selectedClass, setSelectedClass] = useState(null);

  const [highestRisk, setHighestRisk] = useState(null);
  const [topRiskClasses, setTopRiskClasses] = useState([]);

  const [dfsResult, setDfsResult] = useState([]);
  const [bfsResult, setBfsResult] = useState([]);
  const [impactResults, setImpactResults] = useState(null);

  const [currentHistoryClass, setCurrentHistoryClass] =
    useState(null);

  async function loadProjectData() {
    const [
      classData,
      dependencyData,
      highestRiskData,
      topRiskData,
    ] = await Promise.all([
      getAllClasses(),
      getGraphData(),
      getHighestRiskClass(),
      getTopRiskClasses(5),
    ]);

    setClasses(classData || []);
    setGraphData(dependencyData || null);
    setHighestRisk(highestRiskData || null);
    setTopRiskClasses(topRiskData || []);

    setSelectedClass(null);
    setDfsResult([]);
    setBfsResult([]);
    setImpactResults(null);
    setCurrentHistoryClass(null);
  }

  async function handleScan(projectPath) {
    try {
      const message = await scanProject(projectPath);

      await loadProjectData();

      toast.success(message);
    } catch (error) {
      toast.error(
        error.response?.data ||
          error.message ||
          "Project scanning failed."
      );
    }
  }

  async function handleSearch(className) {
    try {
      const result = await searchClass(className);

      setSelectedClass(result);

      const current = await getCurrentHistoryClass();
      setCurrentHistoryClass(current);
    } catch (error) {
      setSelectedClass(null);

      if (error.response?.status === 404) {
        toast.error(`Class not found: ${className}`);
      } else {
        toast.error("Class search failed.");
      }
    }
  }

  async function handleClassSelection(classInfo) {
    if (!classInfo) {
      return;
    }

    await handleSearch(classInfo.className);
  }

  async function handleDFS(className) {
    try {
      const result = await runDFS(className);
      setDfsResult(result || []);
    } catch {
      setDfsResult([]);
      toast.error("DFS traversal failed.");
    }
  }

  async function handleBFS(className) {
    try {
      const result = await runBFS(className);
      setBfsResult(result || []);
    } catch {
      setBfsResult([]);
      toast.error("BFS traversal failed.");
    }
  }

  async function handleImpactAnalysis(className) {
    try {
      const result = await getImpactAnalysis(className);
      setImpactResults(result || []);
    } catch {
      setImpactResults(null);
      toast.error("Impact analysis failed.");
    }
  }

  async function handleBack() {
    try {
      const current = await goBack();
      setCurrentHistoryClass(current);
    } catch {
      toast.error("Could not move backward.");
    }
  }

  async function handleForward() {
    try {
      const current = await goForward();
      setCurrentHistoryClass(current);
    } catch {
      toast.error("Could not move forward.");
    }
  }

  async function handleClearHistory() {
    try {
      await clearHistory();

      setCurrentHistoryClass(null);

      toast.success("Navigation history cleared.");
    } catch {
      toast.error("Could not clear history.");
    }
  }

  return (
    <>
      <Toaster position="top-right" />

      <header className="app-header">
        <div>
          <h1>CodeScope</h1>

          <p>
            Software Architecture Visualizer and Analyzer
          </p>
        </div>

        <div className="header-status">
          {classes.length > 0
            ? `${classes.length} classes loaded`
            : "No project loaded"}
        </div>
      </header>

      <main className="app-layout">
        <ProjectScanner onScan={handleScan} />

        <section className="two-column-layout">
          <ClassSearch
            onSearch={handleSearch}
            disabled={classes.length === 0}
          />

          <NavigationHistory
            currentClass={currentHistoryClass}
            onBack={handleBack}
            onForward={handleForward}
            onClear={handleClearHistory}
          />
        </section>

        <DependencyGraph
          graphData={graphData}
          onNodeClick={handleClassSelection}
        />

        <section className="two-column-layout">
          <ClassList
            classes={classes}
            selectedClass={selectedClass}
            onSelectClass={handleClassSelection}
          />

          <ClassDetails classInfo={selectedClass} />
        </section>

        <section className="three-column-layout">
          <RiskRanking
            highestRisk={highestRisk}
            topRiskClasses={topRiskClasses}
            onSelectClass={handleClassSelection}
          />

          <TraversalPanel
            classes={classes}
            onDFS={handleDFS}
            onBFS={handleBFS}
            dfsResult={dfsResult}
            bfsResult={bfsResult}
          />

          <ImpactAnalysis
            classes={classes}
            results={impactResults}
            onAnalyze={handleImpactAnalysis}
          />
        </section>
      </main>
    </>
  );
}

export default App;