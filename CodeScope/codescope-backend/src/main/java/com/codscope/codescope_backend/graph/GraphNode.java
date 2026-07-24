package com.codscope.codescope_backend.graph;

import com.codscope.codescope_backend.model.ClassInfo;
import java.util.ArrayList;

public class GraphNode {

    private ClassInfo classInfo;

    private ArrayList<GraphNode> neighbours;

    public GraphNode(ClassInfo classInfo) {
        this.classInfo = classInfo;
        this.neighbours = new ArrayList<>();
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public ArrayList<GraphNode> getNeighbours() {
        return neighbours;
    }

    @Override
    public String toString() {
        return classInfo.getClassName();
    }
}