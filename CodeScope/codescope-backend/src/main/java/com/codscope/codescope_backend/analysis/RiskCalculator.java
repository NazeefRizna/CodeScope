package com.codscope.codescope_backend.analysis;

import com.codscope.codescope_backend.graph.Graph;
import com.codscope.codescope_backend.model.ClassInfo;

import java.util.List;

public class RiskCalculator {

    private RiskCalculator() {
    }

    public static void calculateRiskScores(
            List<ClassInfo> classes,
            Graph graph
    ) {

        if (classes == null
                || classes.isEmpty()
                || graph == null) {

            return;
        }

        int totalClasses = classes.size();

        int maxIncoming = 0;
        int maxOutgoing = 0;
        int maxCbo = 0;

        // Find maximum values for normalization
        for (ClassInfo classInfo : classes) {

            int incoming =
                    classInfo.getIncomingDependencies();

            int outgoing =
                    classInfo.getOutgoingDependencies();

            int cbo =
                    incoming + outgoing;

            maxIncoming =
                    Math.max(maxIncoming, incoming);

            maxOutgoing =
                    Math.max(maxOutgoing, outgoing);

            maxCbo =
                    Math.max(maxCbo, cbo);
        }

        // Calculate CRI for each class
        for (ClassInfo classInfo : classes) {

            int incoming =
                    classInfo.getIncomingDependencies();

            int outgoing =
                    classInfo.getOutgoingDependencies();

            int cbo =
                    incoming + outgoing;

            int affectedCount =
                    graph.getAffectedClassCount(
                            classInfo.getClassName()
                    );

            double normalizedIncoming =
                    maxIncoming == 0
                            ? 0
                            : (double) incoming / maxIncoming;

            double normalizedOutgoing =
                    maxOutgoing == 0
                            ? 0
                            : (double) outgoing / maxOutgoing;

            double normalizedCbo =
                    maxCbo == 0
                            ? 0
                            : (double) cbo / maxCbo;

            double normalizedReach =
                    totalClasses <= 1
                            ? 0
                            : (double) affectedCount
                            / (totalClasses - 1);

            double cri =
                    100 * (
                            (0.45 * normalizedIncoming)
                                    + (0.30 * normalizedReach)
                                    + (0.15 * normalizedOutgoing)
                                    + (0.10 * normalizedCbo)
                    );

            cri =
                    Math.round(cri * 100.0) / 100.0;

            classInfo.setComponentRiskIndex(cri);
        }
    }
}