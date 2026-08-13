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

        int totalClasses =
                classes.size();

        int maxIncoming = 0;
        int maxCbo = 0;

        // Find maximum values for normalization
        for (ClassInfo classInfo : classes) {

            int incoming =
                    classInfo
                            .getIncomingDependencies();

            int cbo =
                    graph.getCBO(
                            classInfo.getClassName()
                    );

            maxIncoming =
                    Math.max(
                            maxIncoming,
                            incoming
                    );

            maxCbo =
                    Math.max(
                            maxCbo,
                            cbo
                    );
        }

        // Calculate CRI for each class
        for (ClassInfo classInfo : classes) {

            int incoming =
                    classInfo
                            .getIncomingDependencies();

            // Number of distinct classes coupled
            // with this class
            int cbo =
                    graph.getCBO(
                            classInfo.getClassName()
                    );

            // Number of classes that could be
            // affected if this class changes
            int affectedCount =
                    graph.getAffectedClassCount(
                            classInfo.getClassName()
                    );

            // Normalize incoming dependencies
            double normalizedIncoming =
                    maxIncoming == 0
                            ? 0
                            : (double) incoming
                            / maxIncoming;

            // Normalize CBO
            double normalizedCbo =
                    maxCbo == 0
                            ? 0
                            : (double) cbo
                            / maxCbo;

            // Normalize affected-class reach
            double normalizedReach =
                    totalClasses <= 1
                            ? 0
                            : (double) affectedCount
                            / (totalClasses - 1);

            // Calculate Component Risk Index
            double cri =
                    100 * (
                            (0.50 * normalizedIncoming)
                                    + (0.30 * normalizedReach)
                                    + (0.20 * normalizedCbo)
                    );

            // Round CRI to 2 decimal places
            cri =
                    Math.round(
                            cri * 100.0
                    ) / 100.0;

            // Store calculated CRI
            classInfo
                    .setComponentRiskIndex(
                            cri
                    );
        }
    }
}