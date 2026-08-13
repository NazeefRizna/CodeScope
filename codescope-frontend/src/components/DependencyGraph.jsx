import { useEffect, useMemo } from "react";
import dagre from "@dagrejs/dagre";
import { toPng } from "html-to-image";
import { jsPDF } from "jspdf";

import {
  Background,
  Controls,
  MarkerType,
  Position,
  ReactFlow,
  ReactFlowProvider,
  getNodesBounds,
  getViewportForBounds,
  useEdgesState,
  useNodesState,
  useReactFlow,
} from "@xyflow/react";

import "@xyflow/react/dist/style.css";

const NODE_WIDTH = 220;
const NODE_HEIGHT = 100;

function createGraphLayout(nodes, edges) {
  const dagreGraph =
    new dagre.graphlib.Graph();

  dagreGraph.setDefaultEdgeLabel(
    () => ({})
  );

  dagreGraph.setGraph({
    rankdir: "TB",
    ranksep: 120,
    nodesep: 90,
    edgesep: 50,
    marginx: 40,
    marginy: 40,
  });

  nodes.forEach((node) => {
    dagreGraph.setNode(
      node.id,
      {
        width: NODE_WIDTH,
        height: NODE_HEIGHT,
      }
    );
  });

  edges.forEach((edge) => {
    dagreGraph.setEdge(
      edge.source,
      edge.target
    );
  });

  dagre.layout(dagreGraph);

  const layoutedNodes =
    nodes.map((node) => {
      const position =
        dagreGraph.node(node.id);

      return {
        ...node,

        targetPosition:
          Position.Top,

        sourcePosition:
          Position.Bottom,

        position: {
          x:
            position.x -
            NODE_WIDTH / 2,

          y:
            position.y -
            NODE_HEIGHT / 2,
        },

        draggable: true,
      };
    });

  return {
    nodes: layoutedNodes,
    edges,
  };
}

function DependencyGraphContent({
  graphData,
  onNodeClick,
}) {
  const {
    getNodes,
  } = useReactFlow();

  const initialGraph =
    useMemo(() => {

      const rawNodes =
        (graphData?.nodes || []).map(
          (classInfo) => ({
            id:
              classInfo.fullClassName,

            data: {
              label: (
                <div className="graph-node-content">

                  <strong>
                    {classInfo.className}
                  </strong>

                  <small>
                    {classInfo.packageName ||
                      "Default package"}
                  </small>

                  <div className="graph-node-metrics">

                    <span>
                      In:{" "}
                      {
                        classInfo
                          .incomingDependencies
                      }
                    </span>

                    <span>
                      Out:{" "}
                      {
                        classInfo
                          .outgoingDependencies
                      }
                    </span>

                    <span>
                      CRI:{" "}
                      {
                        classInfo
                          .componentRiskIndex
                      }
                    </span>

                  </div>

                </div>
              ),

              classInfo,
            },

            style: {
              width: NODE_WIDTH,
              minHeight:
                NODE_HEIGHT,
              padding: 12,
              borderRadius: 12,
              border:
                "1px solid #94a3b8",
              background:
                "#ffffff",
              boxShadow:
                "0 5px 14px rgba(15, 23, 42, 0.10)",
            },
          })
        );

      const rawEdges =
        (graphData?.edges || []).map(
          (edge, index) => ({
            id:
              `${edge.source}-${edge.target}-${index}`,

            source:
              edge.source,

            target:
              edge.target,

            type:
              "smoothstep",

            markerEnd: {
              type:
                MarkerType.ArrowClosed,
              width: 20,
              height: 20,
            },

            style: {
              stroke:
                "#334155",
              strokeWidth: 2,
            },
          })
        );

      return createGraphLayout(
        rawNodes,
        rawEdges
      );

    }, [graphData]);

  const [
    nodes,
    setNodes,
    onNodesChange,
  ] =
    useNodesState(
      initialGraph.nodes
    );

  const [
    edges,
    setEdges,
    onEdgesChange,
  ] =
    useEdgesState(
      initialGraph.edges
    );

  useEffect(() => {

    setNodes(
      initialGraph.nodes
    );

    setEdges(
      initialGraph.edges
    );

  }, [
    initialGraph,
    setNodes,
    setEdges,
  ]);

  async function downloadGraphAsPDF() {
    try {

      const currentNodes =
        getNodes();

      if (
        !currentNodes ||
        currentNodes.length === 0
      ) {
        return;
      }

      const bounds =
        getNodesBounds(
          currentNodes
        );

      /*
       * Give the exported graph some padding.
       */
      const padding = 80;

      const exportWidth =
        Math.max(
          bounds.width +
            padding * 2,
          800
        );

      const exportHeight =
        Math.max(
          bounds.height +
            padding * 2,
          600
        );

      /*
       * Calculate the viewport needed
       * to display the complete graph.
       */
      const viewport =
        getViewportForBounds(
          bounds,
          exportWidth,
          exportHeight,
          0.1,
          2,
          0.9
        );

      const viewportElement =
        document.querySelector(
          ".react-flow__viewport"
        );

      if (!viewportElement) {
        return;
      }

      /*
       * Export the entire viewport,
       * not only what is currently visible.
       */
      const dataUrl =
        await toPng(
          viewportElement,
          {
            backgroundColor:
              "#ffffff",

            width:
              exportWidth,

            height:
              exportHeight,

            pixelRatio: 2,

            style: {
              width:
                `${exportWidth}px`,

              height:
                `${exportHeight}px`,

              transform:
                `translate(${viewport.x}px, ${viewport.y}px) scale(${viewport.zoom})`,

              transformOrigin:
                "top left",
            },
          }
        );

      /*
       * Landscape is usually better
       * for dependency graphs.
       */
      const orientation =
        exportWidth >
        exportHeight
          ? "landscape"
          : "portrait";

      const pdf =
        new jsPDF({
          orientation,
          unit: "px",
          format: [
            exportWidth,
            exportHeight,
          ],
          hotfixes: [
            "px_scaling",
          ],
        });

      pdf.addImage(
        dataUrl,
        "PNG",
        0,
        0,
        exportWidth,
        exportHeight
      );

      pdf.save(
        "codescope-dependency-graph.pdf"
      );

    } catch (error) {

      console.error(
        "PDF export failed:",
        error
      );
    }
  }

  if (
    !graphData ||
    graphData.nodes?.length === 0
  ) {
    return (
      <section className="panel">

        <h2>
          Dependency Graph
        </h2>

        <p className="muted">
          Scan a project to display
          its dependency graph.
        </p>

      </section>
    );
  }

  return (
    <section className="panel graph-panel">

      <div className="section-heading">

        <div>

          <h2>
            Dependency Graph
          </h2>

          <p className="muted">
            Drag nodes to rearrange the
            graph and click a class to
            view its details.
          </p>

        </div>

        <div className="graph-statistics">

          <span>
            {graphData.nodeCount}
            {" "}classes
          </span>

          <span>
            {graphData.edgeCount}
            {" "}dependencies
          </span>

          <button
            type="button"
            className="primary-button"
            onClick={
              downloadGraphAsPDF
            }
          >
            Save Graph as PDF
          </button>

        </div>

      </div>

      <div className="graph-container">

        <ReactFlow
          nodes={nodes}
          edges={edges}
          onNodesChange={
            onNodesChange
          }
          onEdgesChange={
            onEdgesChange
          }
          nodesDraggable={true}
          nodesConnectable={false}
          elementsSelectable={true}
          fitView
          fitViewOptions={{
            padding: 0.25,
          }}
          minZoom={0.2}
          maxZoom={1.8}
          onNodeClick={
            (_, node) => {
              onNodeClick?.(
                node.data.classInfo
              );
            }
          }
        >

          <Background
            gap={20}
            size={1}
          />

          <Controls />

        </ReactFlow>

      </div>

    </section>
  );
}

function DependencyGraph(props) {
  return (
    <ReactFlowProvider>
      <DependencyGraphContent
        {...props}
      />
    </ReactFlowProvider>
  );
}

export default DependencyGraph;