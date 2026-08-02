import { useState } from "react";

import ProjectScanner from "../components/ProjectScanner";
import ProjectSummary from "../components/ProjectSummary";
import ClassSearch from "../components/ClassSearch";
import ClassDetails from "../components/ClassDetails";
import RiskRanking from "../components/RiskRanking";

import {
  scanProject,
  getAllClasses,
  searchClass,
  getHighestRiskClass,
  getTopRiskClasses,
} from "../api/codescopeApi";

function Dashboard() {
  const [classes, setClasses] = useState([]);
  const [selectedClass, setSelectedClass] = useState(null);
  const [highestRisk, setHighestRisk] = useState(null);
  const [topRiskClasses, setTopRiskClasses] = useState([]);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  async function handleScan(projectPath) {
    try {
      setError("");
      setMessage("");

      const scanMessage = await scanProject(projectPath);

      const [
        classData,
        highestRiskData,
        topRiskData,
      ] = await Promise.all([
        getAllClasses(),
        getHighestRiskClass(),
        getTopRiskClasses(5),
      ]);

      setClasses(classData || []);
      setHighestRisk(highestRiskData || null);
      setTopRiskClasses(topRiskData || []);
      setSelectedClass(null);
      setMessage(scanMessage);
    } catch (requestError) {
      setError(
        requestError.response?.data ||
          requestError.message ||
          "Project scanning failed."
      );
    }
  }

  async function handleSearch(className) {
    try {
      setError("");

      const result = await searchClass(className);

      setSelectedClass(result);
    } catch (requestError) {
      setSelectedClass(null);

      if (requestError.response?.status === 404) {
        setError(`Class not found: ${className}`);
      } else {
        setError("Class search failed.");
      }
    }
  }

  return (
    <main className="dashboard">
      <header className="page-header">
        <h1>CodeScope</h1>

        <p>
          Software architecture visualization and analysis tool
        </p>
      </header>

      {message && (
        <div className="message success">{message}</div>
      )}

      {error && (
        <div className="message error">{error}</div>
      )}

      <ProjectScanner onScan={handleScan} />

      <ClassSearch
        onSearch={handleSearch}
        disabled={classes.length === 0}
      />

      <ClassDetails classInfo={selectedClass} />

      <RiskRanking
        highestRisk={highestRisk}
        topRiskClasses={topRiskClasses}
      />

      <ProjectSummary classes={classes} />
    </main>
  );
}

export default Dashboard;
