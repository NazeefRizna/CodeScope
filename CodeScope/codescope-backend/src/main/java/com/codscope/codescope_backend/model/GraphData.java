package com.codscope.codescope_backend.model;

import java.util.List;

public class GraphData {

    private final List<ClassInfo> nodes;
    private final List<GraphEdge> edges;

    public GraphData(
            List<ClassInfo> nodes,
            List<GraphEdge> edges
    ) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public List<ClassInfo> getNodes() {
        return nodes;
    }

    public List<GraphEdge> getEdges() {
        return edges;
    }
}