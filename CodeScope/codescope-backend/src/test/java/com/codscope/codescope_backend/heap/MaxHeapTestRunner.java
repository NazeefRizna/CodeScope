package com.codscope.codescope_backend.heap;

import com.codscope.codescope_backend.analysis.RiskCalculator;
import com.codscope.codescope_backend.model.ClassInfo;

public class MaxHeapTestRunner {

    public static void main(String[] args) {

        MaxHeap maxHeap = new MaxHeap();

        // Sample ClassInfo objects
        ClassInfo paymentService = new ClassInfo("PaymentService", "service", "PaymentService.java");

        ClassInfo bookingService = new ClassInfo("BookingService", "service", "BookingService.java");

        ClassInfo databaseManager = new ClassInfo("DatabaseManager", "repository", "DatabaseManager.java");

        // Set incoming and outgoing dependencies
        paymentService.setIncomingDependencies(5);
        paymentService.setOutgoingDependencies(3);

        bookingService.setIncomingDependencies(3);
        bookingService.setOutgoingDependencies(2);

        databaseManager.setIncomingDependencies(8);
        databaseManager.setOutgoingDependencies(1);

        // Calculate risk values
        double paymentRisk = RiskCalculator.calculateRisk(paymentService);
        double bookingRisk = RiskCalculator.calculateRisk(bookingService);
        double databaseRisk = RiskCalculator.calculateRisk(databaseManager);

        // Insert classes into Max Heap
        maxHeap.insert(paymentService, paymentRisk);
        maxHeap.insert(bookingService, bookingRisk);
        maxHeap.insert(databaseManager, databaseRisk);

        // Display calculated CRI values
        System.out.println("===== CRI VALUES =====");

        System.out.println("PaymentService CRI: " + paymentRisk);
        System.out.println("BookingService CRI: " + bookingRisk);
        System.out.println("DatabaseManager CRI: " + databaseRisk);

        // Display highest risk class without removing it
        HeapNode highestRisk = maxHeap.peekMax();

        System.out.println("\n===== HIGHEST RISK CLASS =====");

        System.out.println(highestRisk.getClassInfo().getClassName() + " | CRI: " + highestRisk.getRiskScore());

        // Remove classes according to risk order
        System.out.println("\n===== REMOVE BY RISK ORDER =====");

        HeapNode first = maxHeap.extractMax();

        System.out.println(first.getClassInfo().getClassName() + " | CRI: " + first.getRiskScore());

        HeapNode second = maxHeap.extractMax();

        System.out.println(second.getClassInfo().getClassName() + " | CRI: " + second.getRiskScore());

        HeapNode third = maxHeap.extractMax();

        System.out.println(third.getClassInfo().getClassName() + " | CRI: " + third.getRiskScore());

        System.out.println("\n===== MAX HEAP TEST COMPLETED =====");
    }
}