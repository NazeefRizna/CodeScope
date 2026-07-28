package com.codscope.codescope_backend.graph ;

import com.codscope.codescope_backend.model.ClassInfo;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    private final ArrayList<GraphNode> nodes;

    public Graph() {
        nodes = new ArrayList<>();
    }

    public void createNodes(List<ClassInfo> scannedClasses) {

        for (ClassInfo classInfo : scannedClasses) {
            addClass(classInfo);
        }
    }

    public void addClass(ClassInfo classInfo) {

        if (classInfo == null) {
            return;
        }

        if (!containsClass(classInfo.getFullClassName())) {
            nodes.add(new GraphNode(classInfo));
        }
    }

    public boolean addDependency(String sourceClassName,
                                 String destinationClassName) {

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

    public GraphNode getNode(String className) {

        if (className == null) {
            return null;
        }

        for (GraphNode node : nodes) {

            ClassInfo info = node.getClassInfo();

            if (info.getClassName().equals(className) ||
                    info.getFullClassName().equals(className)) {

                return node;
            }
        }

        return null;
    }

    public boolean containsClass(String className) {
        return getNode(className) != null;
    }

    public ArrayList<GraphNode> getAllNodes() {
        return nodes;
    }

    public void displayGraph() {

        System.out.println("\n----- DEPENDENCY GRAPH -----\n");

        for (GraphNode node : nodes) {

            System.out.print(
                    node.getClassInfo().getClassName() + " → "
            );

            if (node.getDependencies().isEmpty()) {
                System.out.println("No dependencies");
                continue;
            }

            for (int i = 0;
                 i < node.getDependencies().size();
                 i++) {

                GraphNode dependency =
                        node.getDependencies().get(i);

                System.out.print(
                        dependency.getClassInfo().getClassName()
                );

                if (i < node.getDependencies().size() - 1) {
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

            ClassInfo info = node.getClassInfo();

            System.out.println(
                    "Class      : " + info.getClassName()
            );

            System.out.println(
                    "Incoming   : " +
                            info.getIncomingDependencies()
            );

            System.out.println(
                    "Outgoing   : " +
                            info.getOutgoingDependencies()
            );

            System.out.println(
                    "-----------------------------------"
            );
        }
    }
}