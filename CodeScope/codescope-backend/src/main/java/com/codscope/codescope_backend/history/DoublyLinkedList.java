package com.codscope.codescope_backend.history;

public class DoublyLinkedList {

    private HistoryNode head;
    private HistoryNode tail;
    private HistoryNode current;
    private int size;

    public DoublyLinkedList() {

        head = null;
        tail = null;
        current = null;
        size = 0;
    }

    // Visit a new class
    public void visitClass(String className) {

        if (className == null || className.isBlank()) {
            return;
        }

        // Prevent opening the same class twice consecutively
        if (current != null &&
                current.getClassName().equals(className)) {
            return;
        }

        HistoryNode newNode = new HistoryNode(className);

        // First visited class
        if (head == null) {

            head = newNode;
            tail = newNode;
            current = newNode;
            size++;

            return;
        }

        // Remove forward history if user previously moved back
        if (current != tail) {

            HistoryNode temp = current.getNext();

            while (temp != null) {

                HistoryNode next = temp.getNext();

                temp.setPrevious(null);
                temp.setNext(null);

                temp = next;
                size--;
            }

            current.setNext(null);
            tail = current;
        }

        tail.setNext(newNode);
        newNode.setPrevious(tail);

        tail = newNode;
        current = newNode;

        size++;
    }

    // Move backward
    public String goBack() {

        if (current != null &&
                current.getPrevious() != null) {

            current = current.getPrevious();
        }

        return getCurrentClass();
    }

    // Move forward
    public String goForward() {

        if (current != null &&
                current.getNext() != null) {

            current = current.getNext();
        }

        return getCurrentClass();
    }

    // Current class
    public String getCurrentClass() {

        if (current == null) {
            return null;
        }

        return current.getClassName();
    }

    // Display complete history
    public void displayHistory() {

        if (head == null) {

            System.out.println("History is empty.");
            return;
        }

        System.out.println("\n----- NAVIGATION HISTORY -----\n");

        HistoryNode temp = head;

        while (temp != null) {

            if (temp == current) {

                System.out.println(
                        temp.getClassName() +
                                "  <-- Current"
                );

            } else {

                System.out.println(
                        temp.getClassName()
                );
            }

            temp = temp.getNext();
        }
    }

    // Remove all history
    public void clearHistory() {

        head = null;
        tail = null;
        current = null;
        size = 0;
    }

    // Check whether history is empty
    public boolean isEmpty() {
        return head == null;
    }

    // Total visited classes
    public int getSize() {
        return size;
    }
}