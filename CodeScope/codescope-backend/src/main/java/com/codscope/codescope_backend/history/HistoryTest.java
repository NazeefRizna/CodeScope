package com.codscope.codescope_backend.history;

public class HistoryTest {

    public static void main(String[] args) {

        System.out.println("History Test Running");



        NavigationHistory history = new NavigationHistory();

        history.openClass("BookingController");
        history.openClass("BookingService");
        history.openClass("PaymentService");
        history.openClass("DatabaseManager");

        history.showHistory();

        System.out.println();

        history.back();
        history.back();

        System.out.println();

        history.showHistory();

        System.out.println();

        history.forward();

        System.out.println();

        history.showHistory();

        System.out.println();

        System.out.println(
                "Current Class : " +
                        history.currentClass()
        );

        System.out.println(
                "Total Visited : " +
                        history.totalVisited()
        );

    }

}