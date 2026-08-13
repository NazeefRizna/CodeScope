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
import ShortestPathPanel from "./components/ShortestPathPanel";

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
  runDFS,
  scanProject,
  searchClass,
  findShortestDependencyPath,
} from "./api/codescopeApi";

import "./App.css";

function App() {
  const [classes, setClasses] = useState([]);
  const [graphData, setGraphData] = useState(null);
  const [selectedClass, setSelectedClass] = useState(null);

  const [highestRisk, setHighestRisk] = useState(null);
  const [topRiskClasses, setTopRiskClasses] = useState([]);

  const [dfsResult, setDfsResult] = useState([]);
  const [shortestPath, setShortestPath] = useState([]);

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
    setShortestPath([]);
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
      const result =
        await searchClass(className);

      setSelectedClass(result);

      const current =
        await getCurrentHistoryClass();

      setCurrentHistoryClass(current);
    } catch (error) {
      setSelectedClass(null);

      if (error.response?.status === 404) {
        toast.error(
          `Class not found: ${className}`
        );
      } else {
        toast.error(
          "Class search failed."
        );
      }
    }
  }

  async function handleClassSelection(classInfo) {
    if (!classInfo) {
      return;
    }

    await handleSearch(
      classInfo.className
    );
  }

  async function handleDFS(className) {
    try {
      const result =
        await runDFS(className);

      setDfsResult(result || []);
    } catch {
      setDfsResult([]);

      toast.error(
        "Could not trace class dependencies."
      );
    }
  }

  async function handleFindShortestPath(
    startClass,
    targetClass
  ) {
    try {
      const result =
        await findShortestDependencyPath(
          startClass,
          targetClass
        );

      setShortestPath(
        result || []
      );
    } catch (error) {
      setShortestPath([]);

      if (error.response?.status === 204) {
        toast.error(
          "No dependency path exists between these classes."
        );
      } else {
        toast.error(
          "Could not find dependency path."
        );
      }
    }
  }

  async function handleImpactAnalysis(
    className
  ) {
    try {
      const result =
        await getImpactAnalysis(
          className
        );

      setImpactResults(
        result || []
      );
    } catch {
      setImpactResults(null);

      toast.error(
        "Impact analysis failed."
      );
    }
  }

  async function handleBack() {
    try {
      const current =
        await goBack();

      if (!current) {
        return;
      }

      setCurrentHistoryClass(
        current
      );

      const foundClass =
        classes.find(
          (classInfo) =>
            classInfo.fullClassName ===
            current
        );

      if (foundClass) {
        setSelectedClass(
          foundClass
        );
      }
    } catch {
      toast.error(
        "Could not move backward."
      );
    }
  }

  async function handleForward() {
    try {
      const current =
        await goForward();

      if (!current) {
        return;
      }

      setCurrentHistoryClass(
        current
      );

      const foundClass =
        classes.find(
          (classInfo) =>
            classInfo.fullClassName ===
            current
        );

      if (foundClass) {
        setSelectedClass(
          foundClass
        );
      }
    } catch {
      toast.error(
        "Could not move forward."
      );
    }
  }

  async function handleClearHistory() {
    try {
      await clearHistory();

      setCurrentHistoryClass(
        null
      );

      toast.success(
        "Navigation history cleared."
      );
    } catch {
      toast.error(
        "Could not clear history."
      );
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

        <ProjectScanner
          onScan={handleScan}
        />

        <ClassSearch
          classes={classes}
          onSearch={handleSearch}
          disabled={classes.length === 0}
        />

        <DependencyGraph
          graphData={graphData}
          onNodeClick={
            handleClassSelection
          }
        />

        <section className="two-column-layout">

          <ClassList
            classes={classes}
            selectedClass={
              selectedClass
            }
            onSelectClass={
              handleClassSelection
            }
          />

          <ClassDetails
            classInfo={
              selectedClass
            }
            onBack={handleBack}
            onForward={
              handleForward
            }
            onClearHistory={
              handleClearHistory
            }
          />

        </section>

        <section className="three-column-layout">

          <RiskRanking
            highestRisk={
              highestRisk
            }
            topRiskClasses={
              topRiskClasses
            }
            onSelectClass={
              handleClassSelection
            }
          />

          <TraversalPanel
            classes={classes}
            onDFS={handleDFS}
            dfsResult={dfsResult}
          />

          <ImpactAnalysis
            classes={classes}
            results={
              impactResults
            }
            onAnalyze={
              handleImpactAnalysis
            }
          />

          <ShortestPathPanel
            classes={classes}
            onFindShortestPath={
              handleFindShortestPath
            }
            shortestPath={
              shortestPath
            }
          />

        </section>

      </main>
    </>
  );
}

export default App;