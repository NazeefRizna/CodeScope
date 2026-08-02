package com.codscope.codescope_backend.heap;

import com.codscope.codescope_backend.model.ClassInfo;

public class HeapNode {

    private ClassInfo classInfo;
    private double riskScore;

    public HeapNode(ClassInfo classInfo, double riskScore) {

        if (classInfo == null)
        {
            throw new IllegalArgumentException("ClassInfo cannot be null.");
        }

        validateRiskScore(riskScore);

        this.classInfo = classInfo;
        this.riskScore = riskScore;
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public double getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(double riskScore) {
        validateRiskScore(riskScore);
        this.riskScore = riskScore;
        this.classInfo.setComponentRiskIndex(this.riskScore);

    }

    private void validateRiskScore(double riskScore) {

        if (riskScore < 0 || !Double.isFinite(riskScore))
        {
            throw new IllegalArgumentException("Risk score must be a non-negative finite number.");
        }
    }

    @Override
    public String toString()
    {
        return classInfo.getClassName() + " | Risk Score: " + riskScore;
    }
}