import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8081/api/codescope",
  timeout: 60000,
});

export async function scanProject(projectPath) {
  const response = await api.post("/scan", null, {
    params: {
      projectPath,
    },
  });

  return response.data;
}

export async function getAllClasses() {
  const response = await api.get("/classes");
  return response.data || [];
}

export async function getGraphData() {
  const response = await api.get("/graph");

  return (
    response.data || {
      nodes: [],
      edges: [],
      nodeCount: 0,
      edgeCount: 0,
    }
  );
}

export async function searchClass(className) {
  const response = await api.get(
    `/classes/search/${encodeURIComponent(className)}`
  );

  return response.data;
}

export async function getHighestRiskClass() {
  const response = await api.get("/risk/highest");
  return response.data || null;
}

export async function getTopRiskClasses(count = 5) {
  const response = await api.get("/risk/top", {
    params: { count },
  });

  return response.data || [];
}

export async function runDFS(className) {
  const response = await api.get(
    `/graph/dfs/${encodeURIComponent(className)}`
  );

  return response.data || [];
}

export async function runBFS(className) {
  const response = await api.get(
    `/graph/bfs/${encodeURIComponent(className)}`
  );

  return response.data || [];
}

export async function getImpactAnalysis(className) {
  const response = await api.get(
    `/impact/${encodeURIComponent(className)}`
  );

  return response.data || [];
}

export async function getCurrentHistoryClass() {
  const response = await api.get("/history/current");
  return response.data || null;
}

export async function goBack() {
  const response = await api.post("/history/back");
  return response.data || null;
}

export async function goForward() {
  const response = await api.post("/history/forward");
  return response.data || null;
}

export async function clearHistory() {
  const response = await api.delete("/history");
  return response.data;
}