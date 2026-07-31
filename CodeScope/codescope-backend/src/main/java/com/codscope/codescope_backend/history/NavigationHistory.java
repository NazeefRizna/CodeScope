package com.codscope.codescope_backend.history;

public class NavigationHistory {

    private final DoublyLinkedList history;

    public NavigationHistory() {
        history = new DoublyLinkedList();
    }

    // Open a class
    public void openClass(String className) {
        history.visitClass(className);
    }

    // Move to previous class
    public String back() {
        return history.goBack();
    }

    // Move to next class
    public String forward() {
        return history.goForward();
    }

    // Get current class
    public String currentClass() {
        return history.getCurrentClass();
    }

    // Display full history
    public void showHistory() {
        history.displayHistory();
    }

    // Number of visited classes
    public int totalVisited() {
        return history.getSize();
    }

    // Clear navigation history
    public void clear() {
        history.clearHistory();
    }

    // Check whether history is empty
    public boolean isEmpty() {
        return history.isEmpty();
    }
}