package com.codscope.codescope_backend.heap;

import com.codscope.codescope_backend.model.ClassInfo;

public class HeapNode {

    private final ClassInfo classInfo;
    private final double riskScore;

    public HeapNode(
            ClassInfo classInfo,
            double riskScore
    ) {

        if (classInfo == null) {
            throw new IllegalArgumentException(
                    "ClassInfo cannot be null."
            );
        }

        if (riskScore < 0
                || !Double.isFinite(riskScore)) {

            throw new IllegalArgumentException(
                    "Risk score must be a non-negative finite number."
            );
        }

        this.classInfo = classInfo;
        this.riskScore = riskScore;
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public double getRiskScore() {
        return riskScore;
    }
}