package com.codscope.codescope_backend.analysis;

import com.codscope.codescope_backend.model.ClassInfo;

public class RiskCalculator {

    private static final double INCOMING_WEIGHT = 3.0;
    private static final double OUTGOING_WEIGHT = 2.0;

    private RiskCalculator() {
    }

    public static double calculateRisk(ClassInfo classInfo) {

        if (classInfo == null) {

            throw new IllegalArgumentException("Class information cannot be null.");
        }

        int incomingDependencies = classInfo.getIncomingDependencies();

        int outgoingDependencies = classInfo.getOutgoingDependencies();

        if (incomingDependencies < 0 || outgoingDependencies < 0) {

            throw new IllegalArgumentException("Dependency counts cannot be negative.");
        }

        double riskScore = (incomingDependencies * INCOMING_WEIGHT) + (outgoingDependencies * OUTGOING_WEIGHT);

        classInfo.setComponentRiskIndex(riskScore);

        return riskScore;
    }
}