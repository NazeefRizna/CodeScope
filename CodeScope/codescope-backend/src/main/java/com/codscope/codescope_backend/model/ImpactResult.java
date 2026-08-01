package com.codscope.codescope_backend.model;

public class ImpactResult {

    private final String className;
    private final String fullClassName;
    private final String impactType;
    private final int impactLevel;

    public ImpactResult(
            String className,
            String fullClassName,
            String impactType,
            int impactLevel
    ) {
        this.className = className;
        this.fullClassName = fullClassName;
        this.impactType = impactType;
        this.impactLevel = impactLevel;
    }

    public String getClassName() {
        return className;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public String getImpactType() {
        return impactType;
    }

    public int getImpactLevel() {
        return impactLevel;
    }
}