package com.codscope.codescope_backend.service;

import com.codscope.codescope_backend.analysis.RiskCalculator;
import com.codscope.codescope_backend.avl.AVLTree;
import com.codscope.codescope_backend.graph.Graph;
import com.codscope.codescope_backend.heap.HeapNode;
import com.codscope.codescope_backend.heap.MaxHeap;
import com.codscope.codescope_backend.history.NavigationHistory;
import com.codscope.codescope_backend.model.ClassInfo;
import com.codscope.codescope_backend.model.GraphData;
import com.codscope.codescope_backend.model.ImpactResult;
import com.codscope.codescope_backend.scanner.JavaCodeScanner;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CodeScopeService {

    private final JavaCodeScanner scanner;

    private Graph graph;
    private AVLTree avlTree;
    private MaxHeap maxHeap;
    private NavigationHistory navigationHistory;

    private List<ClassInfo> scannedClasses;

    public CodeScopeService() {

        scanner = new JavaCodeScanner();

        graph = new Graph();
        avlTree = new AVLTree();
        maxHeap = new MaxHeap();
        navigationHistory = new NavigationHistory();

        scannedClasses = new ArrayList<>();
    }

    // Project scanning and analysis
    public void scanProject(String projectPath) {

        if (projectPath == null || projectPath.isBlank()) {

            throw new IllegalArgumentException(
                    "Project path cannot be empty."
            );
        }

        resetDataStructures();

        scannedClasses =
                scanner.scanProject(projectPath);

        if (scannedClasses == null
                || scannedClasses.isEmpty()) {

            scannedClasses = new ArrayList<>();
            return;
        }

        // Build dependency graph
        graph.createNodes(scannedClasses);

        scanner.buildDependencies(
                scannedClasses,
                graph
        );

        // Build AVL Tree
        buildAVLTree();

        // Calculate Component Risk Index
        RiskCalculator.calculateRiskScores(
                scannedClasses,
                graph
        );

        // Build Max Heap after CRI calculation
        buildMaxHeap();
    }

    // AVL Tree - Class Search
    private void buildAVLTree() {

        for (ClassInfo classInfo : scannedClasses) {

            avlTree.insert(classInfo);
        }
    }

    public ClassInfo searchClass(
            String className
    ) {

        if (className == null
                || className.isBlank()) {

            return null;
        }

        ClassInfo result =
                avlTree.search(className);

        if (result != null) {

            navigationHistory.openClass(
                    result.getFullClassName()
            );
        }

        return result;
    }

    //Risk Ranking
    private void buildMaxHeap() {

        for (ClassInfo classInfo : scannedClasses) {

            maxHeap.insert(
                    classInfo,
                    classInfo.getComponentRiskIndex()
            );
        }
    }

    public HeapNode getHighestRiskClass() {

        if (maxHeap.isEmpty()) {
            return null;
        }

        return maxHeap.peekMax();
    }

    public List<HeapNode> getTopRiskClasses(
            int count
    ) {

        if (maxHeap.isEmpty()) {
            return new ArrayList<>();
        }

        return maxHeap.getTopRiskClasses(count);
    }

    // Dependency Graph
    public GraphData getGraphData() {

        return graph.getGraphData();
    }

    // DFS - Trace dependencies
    public List<ClassInfo> getDFSResult(
            String className
    ) {

        return graph.depthFirstSearch(
                className
        );
    }

    // BFS - Shortest dependency path
    public List<ClassInfo> findShortestDependencyPath(
            String startClassName,
            String targetClassName
    ) {

        if (startClassName == null
                || startClassName.isBlank()
                || targetClassName == null
                || targetClassName.isBlank()) {

            return new ArrayList<>();
        }

        return graph.findShortestDependencyPath(
                startClassName,
                targetClassName
        );
    }

    // Change impact analysis
    public List<ImpactResult> getImpactAnalysis(
            String className
    ) {

        return graph.impactAnalysis(
                className
        );
    }

    public boolean graphContainsClass(
            String className
    ) {

        return graph.getNode(className) != null;
    }

    // Navigation History
    public String goBack() {

        return navigationHistory.back();
    }

    public String goForward() {

        return navigationHistory.forward();
    }

    public String getCurrentClass() {

        return navigationHistory.currentClass();
    }

    public void clearNavigationHistory() {

        navigationHistory.clear();
    }

    // Project Information
    public List<ClassInfo> getScannedClasses() {

        return scannedClasses;
    }

    public int getClassCount() {

        return scannedClasses.size();
    }

    public boolean hasLoadedProject() {

        return !scannedClasses.isEmpty();
    }

    // Reset
    private void resetDataStructures() {

        graph = new Graph();
        avlTree = new AVLTree();
        maxHeap = new MaxHeap();
        navigationHistory =
                new NavigationHistory();

        scannedClasses =
                new ArrayList<>();
    }
}