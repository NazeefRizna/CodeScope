package com.codscope.codescope_backend.graph;

import com.codscope.codescope_backend.model.ClassInfo;
import com.codscope.codescope_backend.model.GraphData;
import com.codscope.codescope_backend.model.GraphEdge;
import com.codscope.codescope_backend.model.ImpactResult;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    private final ArrayList<GraphNode> nodes;

    public Graph() {
        nodes = new ArrayList<>();
    }

    // Create graph nodes from scanned classes
    public void createNodes(List<ClassInfo> scannedClasses) {

        if (scannedClasses == null) {
            return;
        }

        for (ClassInfo classInfo : scannedClasses) {
            addClass(classInfo);
        }
    }

    // Add a class as a graph node
    private void addClass(ClassInfo classInfo) {

        if (classInfo == null) {
            return;
        }

        if (!containsClass(classInfo.getFullClassName())) {
            nodes.add(new GraphNode(classInfo));
        }
    }

    // Add a directed dependency:
    // source class depends on destination class
    public boolean addDependency(
            String sourceClassName,
            String destinationClassName
    ) {

        GraphNode sourceNode =
                getNode(sourceClassName);

        GraphNode destinationNode =
                getNode(destinationClassName);

        if (sourceNode == null
                || destinationNode == null) {

            return false;
        }

        boolean added =
                sourceNode.addDependency(
                        destinationNode
                );

        if (added) {

            sourceNode
                    .getClassInfo()
                    .incrementOutgoingDependencies();

            destinationNode
                    .getClassInfo()
                    .incrementIncomingDependencies();
        }

        return added;
    }

    // Find a graph node by simple or full class name
    public GraphNode getNode(String className) {

        if (className == null
                || className.isBlank()) {

            return null;
        }

        for (GraphNode node : nodes) {

            ClassInfo info =
                    node.getClassInfo();

            if (info.getClassName()
                    .equals(className)
                    || info.getFullClassName()
                    .equals(className)) {

                return node;
            }
        }

        return null;
    }

    private boolean containsClass(
            String className
    ) {

        return getNode(className) != null;
    }

    // Return graph data for frontend visualization
    public GraphData getGraphData() {

        List<ClassInfo> nodeData =
                new ArrayList<>();

        List<GraphEdge> edgeData =
                new ArrayList<>();

        for (GraphNode node : nodes) {

            nodeData.add(
                    node.getClassInfo()
            );

            for (GraphNode dependency :
                    node.getDependencies()) {

                edgeData.add(
                        new GraphEdge(
                                node.getClassInfo()
                                        .getFullClassName(),

                                dependency.getClassInfo()
                                        .getFullClassName()
                        )
                );
            }
        }

        return new GraphData(
                nodeData,
                edgeData
        );
    }

    // DFS - Trace all dependencies
    public List<ClassInfo> depthFirstSearch(
            String startClassName
    ) {

        GraphNode startNode =
                getNode(startClassName);

        if (startNode == null) {
            return new ArrayList<>();
        }

        ArrayList<GraphNode> visited =
                new ArrayList<>();

        List<ClassInfo> traversalResult =
                new ArrayList<>();

        dfsRecursive(
                startNode,
                visited,
                traversalResult
        );

        return traversalResult;
    }

    private void dfsRecursive(
            GraphNode current,
            ArrayList<GraphNode> visited,
            List<ClassInfo> traversalResult
    ) {

        visited.add(current);

        traversalResult.add(
                current.getClassInfo()
        );

        for (GraphNode neighbour :
                current.getDependencies()) {

            if (!visited.contains(neighbour)) {

                dfsRecursive(
                        neighbour,
                        visited,
                        traversalResult
                );
            }
        }
    }

    //Find shortest dependency path
    public List<ClassInfo> findShortestDependencyPath(
            String startClassName,
            String targetClassName
    ) {

        GraphNode startNode =
                getNode(startClassName);

        GraphNode targetNode =
                getNode(targetClassName);

        List<ClassInfo> shortestPath =
                new ArrayList<>();

        if (startNode == null
                || targetNode == null) {

            return shortestPath;
        }

        ArrayList<GraphNode> visited =
                new ArrayList<>();

        ArrayList<GraphNode> queue =
                new ArrayList<>();

        ArrayList<GraphNode> previousNodes =
                new ArrayList<>();

        ArrayList<GraphNode> previousValues =
                new ArrayList<>();

        visited.add(startNode);
        queue.add(startNode);

        int front = 0;

        while (front < queue.size()) {

            GraphNode current =
                    queue.get(front);

            front++;

            if (current == targetNode) {
                break;
            }

            for (GraphNode neighbour :
                    current.getDependencies()) {

                if (!visited.contains(neighbour)) {

                    visited.add(neighbour);
                    queue.add(neighbour);

                    previousNodes.add(neighbour);
                    previousValues.add(current);
                }
            }
        }

        if (!visited.contains(targetNode)) {
            return shortestPath;
        }

        GraphNode current = targetNode;

        while (current != null) {

            shortestPath.add(
                    0,
                    current.getClassInfo()
            );

            if (current == startNode) {
                break;
            }

            int index =
                    previousNodes.indexOf(current);

            if (index == -1) {
                return new ArrayList<>();
            }

            current =
                    previousValues.get(index);
        }

        return shortestPath;
    }

    // Impact Analysis
    public List<ImpactResult> impactAnalysis(
            String changedClassName
    ) {

        GraphNode changedNode =
                getNode(changedClassName);

        if (changedNode == null) {
            return new ArrayList<>();
        }

        ArrayList<GraphNode> visited =
                new ArrayList<>();

        List<ImpactResult> results =
                new ArrayList<>();

        visited.add(changedNode);

        findAffectedClasses(
                changedNode,
                visited,
                results,
                1
        );

        return results;
    }

    private void findAffectedClasses(
            GraphNode changedNode,
            ArrayList<GraphNode> visited,
            List<ImpactResult> results,
            int impactLevel
    ) {

        for (GraphNode possibleAffectedNode :
                nodes) {

            boolean dependsOnChangedNode =
                    possibleAffectedNode
                            .getDependencies()
                            .contains(
                                    changedNode
                            );

            if (dependsOnChangedNode
                    && !visited.contains(
                    possibleAffectedNode
            )) {

                visited.add(
                        possibleAffectedNode
                );

                String impactType =
                        impactLevel == 1
                                ? "Direct"
                                : "Indirect";

                ClassInfo info =
                        possibleAffectedNode
                                .getClassInfo();

                results.add(
                        new ImpactResult(
                                info.getClassName(),
                                info.getFullClassName(),
                                impactType,
                                impactLevel
                        )
                );

                findAffectedClasses(
                        possibleAffectedNode,
                        visited,
                        results,
                        impactLevel + 1
                );
            }
        }
    }

    //CRI calculation
    public int getAffectedClassCount(
            String className
    ) {

        GraphNode changedNode =
                getNode(className);

        if (changedNode == null) {
            return 0;
        }

        ArrayList<GraphNode> visited =
                new ArrayList<>();

        visited.add(changedNode);

        countAffectedClasses(
                changedNode,
                visited
        );

        return visited.size() - 1;
    }

    private void countAffectedClasses(
            GraphNode changedNode,
            ArrayList<GraphNode> visited
    ) {

        for (GraphNode possibleAffectedNode :
                nodes) {

            boolean dependsOnChangedNode =
                    possibleAffectedNode
                            .getDependencies()
                            .contains(
                                    changedNode
                            );

            if (dependsOnChangedNode
                    && !visited.contains(
                    possibleAffectedNode
            )) {

                visited.add(
                        possibleAffectedNode
                );

                countAffectedClasses(
                        possibleAffectedNode,
                        visited
                );
            }
        }
    }
}