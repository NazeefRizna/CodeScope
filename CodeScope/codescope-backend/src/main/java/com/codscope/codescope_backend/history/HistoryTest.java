package com.codscope.codescope_backend.history;

public class HistoryTest {

    public static void main(String[] args) {

        NavigationHistory history = new NavigationHistory();

        history.openClass("BookingController");
        history.openClass("BookingService");
        history.openClass("PaymentService");
        history.openClass("DatabaseManager");

        history.showHistory();

        history.back();
        history.back();

        history.forward();

        System.out.println();

        System.out.println(
                "Current Class : "
                        + history.currentClass()
        );

    }

}