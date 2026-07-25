
package com.codscope.codescope_backend.graph ;

import com.codscope.codescope_backend.model.ClassInfo;

import java.util.ArrayList;

public class GraphNode {

    private final ClassInfo classInfo;
    private final ArrayList<GraphNode> dependencies;

    public GraphNode(ClassInfo classInfo) {
        this.classInfo = classInfo;
        this.dependencies = new ArrayList<>();
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public ArrayList<GraphNode> getDependencies() {
        return dependencies;
    }

    public boolean addDependency(GraphNode dependencyNode) {

        if (dependencyNode == null || dependencyNode == this) {
            return false;
        }

        if (dependencies.contains(dependencyNode)) {
            return false;
        }

        dependencies.add(dependencyNode);
        return true;
    }

    @Override
    public String toString() {
        return classInfo.getClassName();
    }
}