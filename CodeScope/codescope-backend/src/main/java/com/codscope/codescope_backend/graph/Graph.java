package com.codscope.codescope_backend.graph;

import com.codscope.codescope_backend.model.ClassInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class Graph {

    private ArrayList<GraphNode> nodes;

    public Graph() {
        nodes = new ArrayList<>();
    }

    public void addClass(ClassInfo classInfo) {

        if (!containsClass(classInfo.getClassName())) {
            nodes.add(new GraphNode(classInfo));
        }
    }

    public GraphNode getNode(String className) {

        for (GraphNode node : nodes) {

            if (node.getClassInfo()
                    .getClassName()
                    .equals(className)) {

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

        System.out.println("\n-----GRAPH-----\n");

        for (GraphNode node : nodes) {

            ClassInfo info = node.getClassInfo();

            System.out.println("Class      : " + info.getClassName());
            System.out.println("Package    : " + info.getPackageName());
            System.out.println("File Path  : " + info.getFilePath());
            System.out.println("-----------------------------------");
        }

        System.out.println("Total Nodes : " + nodes.size());
    }
}