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
    public void addClass(ClassInfo classInfo) {

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

        GraphNode sourceNode = getNode(sourceClassName);
        GraphNode destinationNode = getNode(destinationClassName);

        if (sourceNode == null || destinationNode == null) {
            return false;
        }

        boolean added =
                sourceNode.addDependency(destinationNode);

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

        if (className == null || className.isBlank()) {
            return null;
        }

        for (GraphNode node : nodes) {

            ClassInfo info = node.getClassInfo();

            if (info.getClassName().equals(className)
                    || info.getFullClassName().equals(className)) {

                return node;
            }
        }

        return null;
    }

    public boolean containsClass(String className) {
        return getNode(className) != null;
    }

    public ArrayList<GraphNode> getAllNodes() {
        return new ArrayList<>(nodes);
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    public int size() {
        return nodes.size();
    }

    public void clear() {
        nodes.clear();
    }

    // --------------------------------------------------
    // Return graph nodes and edges for the controller
    // --------------------------------------------------

    public GraphData getGraphData() {

        List<ClassInfo> nodeData = new ArrayList<>();
        List<GraphEdge> edgeData = new ArrayList<>();

        for (GraphNode node : nodes) {

            nodeData.add(node.getClassInfo());

            for (GraphNode dependency : node.getDependencies()) {

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

        return new GraphData(nodeData, edgeData);
    }

    // --------------------------------------------------
    // Return dependency counts
    // --------------------------------------------------

    public List<ClassInfo> getDependencyCounts() {

        List<ClassInfo> results = new ArrayList<>();

        for (GraphNode node : nodes) {
            results.add(node.getClassInfo());
        }

        return results;
    }

    // --------------------------------------------------
    // DFS returning data
    // --------------------------------------------------

    public List<ClassInfo> depthFirstSearch(
            String startClassName
    ) {

        GraphNode startNode = getNode(startClassName);

        if (startNode == null) {
            return new ArrayList<>();
        }

        ArrayList<GraphNode> visited = new ArrayList<>();
        List<ClassInfo> traversalResult = new ArrayList<>();

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
        traversalResult.add(current.getClassInfo());

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

    // --------------------------------------------------
    // BFS returning data
    // --------------------------------------------------

    public List<ClassInfo> breadthFirstSearch(
            String startClassName
    ) {

        GraphNode startNode = getNode(startClassName);

        if (startNode == null) {
            return new ArrayList<>();
        }

        ArrayList<GraphNode> visited =
                new ArrayList<>();

        ArrayList<GraphNode> queue =
                new ArrayList<>();

        List<ClassInfo> traversalResult =
                new ArrayList<>();

        visited.add(startNode);
        queue.add(startNode);

        int front = 0;

        while (front < queue.size()) {

            GraphNode current = queue.get(front);
            front++;

            traversalResult.add(
                    current.getClassInfo()
            );

            for (GraphNode neighbour :
                    current.getDependencies()) {

                if (!visited.contains(neighbour)) {

                    visited.add(neighbour);
                    queue.add(neighbour);
                }
            }
        }

        return traversalResult;
    }

    // --------------------------------------------------
    // Impact analysis returning data
    // --------------------------------------------------

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

        for (GraphNode possibleAffectedNode : nodes) {

            boolean dependsOnChangedNode =
                    possibleAffectedNode
                            .getDependencies()
                            .contains(changedNode);

            if (dependsOnChangedNode
                    && !visited.contains(
                    possibleAffectedNode
            )) {

                visited.add(possibleAffectedNode);

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

    // --------------------------------------------------
    // Console display methods can remain for testing
    // --------------------------------------------------

    public void displayGraph() {

        System.out.println(
                "\n----- DEPENDENCY GRAPH -----\n"
        );

        for (GraphNode node : nodes) {

            System.out.print(
                    node.getClassInfo()
                            .getClassName()
                            + " → "
            );

            if (node.getDependencies().isEmpty()) {

                System.out.println(
                        "No dependencies"
                );

                continue;
            }

            for (int i = 0;
                 i < node.getDependencies().size();
                 i++) {

                GraphNode dependency =
                        node.getDependencies().get(i);

                System.out.print(
                        dependency.getClassInfo()
                                .getClassName()
                );

                if (i <
                        node.getDependencies().size() - 1) {

                    System.out.print(", ");
                }
            }

            System.out.println();
        }
    }

    public void displayDependencyCounts() {

        System.out.println(
                "\n----- DEPENDENCY COUNTS -----\n"
        );

        for (GraphNode node : nodes) {

            ClassInfo info =
                    node.getClassInfo();

            System.out.println(
                    "Class      : "
                            + info.getClassName()
            );

            System.out.println(
                    "Incoming   : "
                            + info.getIncomingDependencies()
            );

            System.out.println(
                    "Outgoing   : "
                            + info.getOutgoingDependencies()
            );

            System.out.println(
                    "-----------------------------------"
            );
        }
    }
}