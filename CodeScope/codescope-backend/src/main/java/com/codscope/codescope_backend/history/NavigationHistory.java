package com.codscope.codescope_backend.history;

public class NavigationHistory {

    private DoublyLinkedList history;

    public NavigationHistory() {

        history = new DoublyLinkedList();

    }

    public void openClass(String className) {

        history.visitClass(className);

    }

    public void back() {

        history.goBack();

    }

    public void forward() {

        history.goForward();

    }

    public void showHistory() {

        history.displayHistory();

    }

    public String currentClass() {

        return history.getCurrentClass();

    }

}