package com.codscope.codescope_backend.service;

import com.codscope.codescope_backend.model.GraphData;
import com.codscope.codescope_backend.model.ImpactResult;
import com.codscope.codescope_backend.avl.AVLTree;
import com.codscope.codescope_backend.graph.Graph;
import com.codscope.codescope_backend.heap.HeapNode;
import com.codscope.codescope_backend.heap.MaxHeap;
import com.codscope.codescope_backend.history.NavigationHistory;
import com.codscope.codescope_backend.model.ClassInfo;
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

        this.scanner = new JavaCodeScanner();

        this.graph = new Graph();
        this.avlTree = new AVLTree();
        this.maxHeap = new MaxHeap();
        this.navigationHistory = new NavigationHistory();

        this.scannedClasses = new ArrayList<>();
    }

    //Project analysis
    public void scanProject(String projectPath) {

        if (projectPath == null || projectPath.isBlank()) {
            throw new IllegalArgumentException(
                    "Project path cannot be empty."
            );
        }

        resetDataStructures();

        System.out.println(
                "\nScanning project: " + projectPath
        );

        //Scan Java files and create ClassInfo objects
        scannedClasses = scanner.scanProject(projectPath);

        if (scannedClasses == null || scannedClasses.isEmpty()) {

            scannedClasses = new ArrayList<>();

            System.out.println(
                    "No Java classes were found."
            );

            return;
        }

        //Create graph nodes
        graph.createNodes(scannedClasses);

        //Detect and create graph dependencies
        scanner.buildDependencies(
                scannedClasses,
                graph
        );

        //Build AVL Tree
        buildAVLTree();

        //Calculate Component Risk Index
        calculateComponentRiskIndexes();

        //Build Max Heap after calculating CRI
        buildMaxHeap();

        System.out.println(
                "\nProject analysis completed."
        );

        System.out.println(
                "Total classes: " + scannedClasses.size()
        );
    }

    // AVL Tree
    private void buildAVLTree() {

        for (ClassInfo classInfo : scannedClasses) {
            avlTree.insert(classInfo);
        }
    }

    public ClassInfo searchClass(String className) {

        if (className == null || className.isBlank()) {
            return null;
        }

        ClassInfo result = avlTree.search(className);

        if (result != null) {

            navigationHistory.openClass(
                    result.getFullClassName()
            );
        }

        return result;
    }

    public void displayAVLTree() {
        avlTree.displayInOrder();
    }

    public int getAVLTreeHeight() {
        return avlTree.height();
    }

    // Component Risk Index
    private void calculateComponentRiskIndexes() {

        for (ClassInfo classInfo : scannedClasses) {

            double riskScore =
                    calculateComponentRiskIndex(classInfo);

            classInfo.setComponentRiskIndex(riskScore);
        }
    }

    private double calculateComponentRiskIndex(
            ClassInfo classInfo
    ) {

        int incoming =
                classInfo.getIncomingDependencies();

        int outgoing =
                classInfo.getOutgoingDependencies();

        return (incoming * 3.0)
                + (outgoing * 2.0);
    }

    public void displayRiskScores() {

        System.out.println(
                "\n----- COMPONENT RISK SCORES -----\n"
        );

        for (ClassInfo classInfo : scannedClasses) {

            System.out.println(
                    "Class    : " + classInfo.getClassName()
            );

            System.out.println(
                    "Incoming : "
                            + classInfo.getIncomingDependencies()
            );

            System.out.println(
                    "Outgoing : "
                            + classInfo.getOutgoingDependencies()
            );

            System.out.println(
                    "CRI      : "
                            + classInfo.getComponentRiskIndex()
            );

            System.out.println(
                    "-----------------------------------"
            );
        }
    }

    // Max Heap
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

    public List<HeapNode> getTopRiskClasses(int count) {

        if (maxHeap.isEmpty()) {
            return new ArrayList<>();
        }

        return maxHeap.getTopRiskClasses(count);
    }

    public void displayHeap() {
        maxHeap.displayHeap();
    }

    public void displayTopRiskClasses(int count) {
        maxHeap.displayTopRiskClasses(count);
    }

    // Graph
    public GraphData getGraphData() {
        return graph.getGraphData();
    }

    public List<ClassInfo> getDFSResult(
            String className
    ) {
        return graph.depthFirstSearch(className);
    }

    public List<ClassInfo> getBFSResult(
            String className
    ) {
        return graph.breadthFirstSearch(className);
    }

    public List<ImpactResult> getImpactAnalysis(
            String className
    ) {
        return graph.impactAnalysis(className);
    }
    public Graph getGraph() {
        return graph;
    }

    public void displayDependencyGraph() {
        graph.displayGraph();
    }

    public void displayDependencyCounts() {
        graph.displayDependencyCounts();
    }

    public void runDFS(String className) {
        graph.depthFirstSearch(className);
    }

    public void runImpactAnalysis(String className) {
        graph.impactAnalysis(className);
    }

    // Navigation History
    public void openClass(String className) {

        ClassInfo classInfo = searchClass(className);

        if (classInfo == null) {
            System.out.println(
                    "Class not found: " + className
            );
        }
    }

    public String goBack() {
        return navigationHistory.back();
    }

    public String goForward() {
        return navigationHistory.forward();
    }

    public String getCurrentClass() {
        return navigationHistory.currentClass();
    }

    public void displayNavigationHistory() {
        navigationHistory.showHistory();
    }

    public void clearNavigationHistory() {
        navigationHistory.clear();
    }

    // Project data
    public List<ClassInfo> getScannedClasses() {
        return scannedClasses;
    }

    public int getClassCount() {
        return scannedClasses.size();
    }

    public boolean hasLoadedProject() {
        return !scannedClasses.isEmpty();
    }

    // Reset before scanning another project
    private void resetDataStructures() {

        graph = new Graph();
        avlTree = new AVLTree();
        maxHeap = new MaxHeap();
        navigationHistory = new NavigationHistory();

        scannedClasses = new ArrayList<>();
    }
}