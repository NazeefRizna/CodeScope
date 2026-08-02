import { useEffect, useMemo } from "react";
import dagre from "@dagrejs/dagre";

import {
  Background,
  Controls,
  MarkerType,
  Position,
  ReactFlow,
  useEdgesState,
  useNodesState,
} from "@xyflow/react";

import "@xyflow/react/dist/style.css";

const NODE_WIDTH = 220;
const NODE_HEIGHT = 100;

function createGraphLayout(nodes, edges) {
  const dagreGraph = new dagre.graphlib.Graph();

  dagreGraph.setDefaultEdgeLabel(() => ({}));

  dagreGraph.setGraph({
    rankdir: "TB",
    ranksep: 120,
    nodesep: 90,
    edgesep: 50,
    marginx: 40,
    marginy: 40,
  });

  nodes.forEach((node) => {
    dagreGraph.setNode(node.id, {
      width: NODE_WIDTH,
      height: NODE_HEIGHT,
    });
  });

  edges.forEach((edge) => {
    dagreGraph.setEdge(edge.source, edge.target);
  });

  dagre.layout(dagreGraph);

  const layoutedNodes = nodes.map((node) => {
    const position = dagreGraph.node(node.id);

    return {
      ...node,

      targetPosition: Position.Top,
      sourcePosition: Position.Bottom,

      position: {
        x: position.x - NODE_WIDTH / 2,
        y: position.y - NODE_HEIGHT / 2,
      },

      draggable: true,
    };
  });

  return {
    nodes: layoutedNodes,
    edges,
  };
}

function DependencyGraph({ graphData, onNodeClick }) {
  const initialGraph = useMemo(() => {
    const rawNodes = (graphData?.nodes || []).map(
      (classInfo) => ({
        id: classInfo.fullClassName,

        data: {
          label: (
            <div className="graph-node-content">
              <strong>{classInfo.className}</strong>

              <small>
                {classInfo.packageName || "Default package"}
              </small>

              <div className="graph-node-metrics">
                <span>
                  In: {classInfo.incomingDependencies}
                </span>

                <span>
                  Out: {classInfo.outgoingDependencies}
                </span>

                <span>
                  CRI: {classInfo.componentRiskIndex}
                </span>
              </div>
            </div>
          ),

          classInfo,
        },

        style: {
          width: NODE_WIDTH,
          minHeight: NODE_HEIGHT,
          padding: 12,
          borderRadius: 12,
          border: "1px solid #94a3b8",
          background: "#ffffff",
          boxShadow: "0 5px 14px rgba(15, 23, 42, 0.10)",
        },
      })
    );

    const rawEdges = (graphData?.edges || []).map(
      (edge, index) => ({
        id: `${edge.source}-${edge.target}-${index}`,
        source: edge.source,
        target: edge.target,

        type: "smoothstep",

        markerEnd: {
          type: MarkerType.ArrowClosed,
          width: 20,
          height: 20,
        },

        style: {
          stroke: "#334155",
          strokeWidth: 2,
        },
      })
    );

    return createGraphLayout(rawNodes, rawEdges);
  }, [graphData]);

  const [nodes, setNodes, onNodesChange] =
    useNodesState(initialGraph.nodes);

  const [edges, setEdges, onEdgesChange] =
    useEdgesState(initialGraph.edges);

  useEffect(() => {
    setNodes(initialGraph.nodes);
    setEdges(initialGraph.edges);
  }, [initialGraph, setNodes, setEdges]);

  if (!graphData || graphData.nodes?.length === 0) {
    return (
      <section className="panel">
        <h2>Dependency Graph</h2>

        <p className="muted">
          Scan a project to display its dependency graph.
        </p>
      </section>
    );
  }

  return (
    <section className="panel graph-panel">
      <div className="section-heading">
        <div>
          <h2>Dependency Graph</h2>

          <p className="muted">
            Drag nodes to rearrange the graph and click a class
            to view its details.
          </p>
        </div>

        <div className="graph-statistics">
          <span>{graphData.nodeCount} classes</span>
          <span>{graphData.edgeCount} dependencies</span>
        </div>
      </div>

      <div className="graph-container">
        <ReactFlow
          nodes={nodes}
          edges={edges}
          onNodesChange={onNodesChange}
          onEdgesChange={onEdgesChange}
          nodesDraggable={true}
          nodesConnectable={false}
          elementsSelectable={true}
          fitView
          fitViewOptions={{
            padding: 0.25,
          }}
          minZoom={0.2}
          maxZoom={1.8}
          onNodeClick={(_, node) => {
            onNodeClick?.(node.data.classInfo);
          }}
        >
          <Background gap={20} size={1} />
          <Controls />
        </ReactFlow>
      </div>
    </section>
  );
}

export default DependencyGraph;