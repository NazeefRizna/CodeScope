package com.codscope.codescope_backend.history;

public class NavigationHistory {

    private final DoublyLinkedList history;

    public NavigationHistory() {

        history = new DoublyLinkedList();
    }

    public void openClass(
            String className
    ) {

        history.visitClass(className);
    }

    public String back() {

        return history.goBack();
    }

    public String forward() {

        return history.goForward();
    }

    public String currentClass() {

        return history.getCurrentClass();
    }

    public void clear() {

        history.clearHistory();
    }
}