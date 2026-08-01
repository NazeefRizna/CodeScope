package com.codscope.codescope_backend.model;

public class GraphEdge {

    private final String source;
    private final String target;

    public GraphEdge(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}