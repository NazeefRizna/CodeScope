package com.codscope.codescope_backend.model;

public class ClassInfo {
    private String className;
    private String packageName;
    private String filePath;

    private int incomingDependencies;
    private int outgoingDependencies;

    private double componentRiskIndex;

    public ClassInfo() {
    }

    public ClassInfo(String className, String packageName, String filePath) {
        this.className = className;
        this.packageName = packageName;
        this.filePath = filePath;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getIncomingDependencies() {
        return incomingDependencies;
    }

    public void setIncomingDependencies(int incomingDependencies) {
        this.incomingDependencies = incomingDependencies;
    }

    public int getOutgoingDependencies() {
        return outgoingDependencies;
    }

    public void setOutgoingDependencies(int outgoingDependencies) {
        this.outgoingDependencies = outgoingDependencies;
    }

    public double getComponentRiskIndex() {
        return componentRiskIndex;
    }

    public void setComponentRiskIndex(double componentRiskIndex) {
        this.componentRiskIndex = componentRiskIndex;
    }

    @Override
    public String toString() {
        return className;
    }

    public String getFullClassName() {
        return packageName + "." + className;
    }

}
