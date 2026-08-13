package com.codscope.codescope_backend.history;

public class DoublyLinkedList {

    private HistoryNode head;
    private HistoryNode tail;
    private HistoryNode current;

    public void visitClass(String className) {

        if (className == null || className.isBlank()) {
            return;
        }

        // Prevent same class being added twice consecutively
        if (current != null
                && current.getClassName().equals(className)) {
            return;
        }

        HistoryNode newNode =
                new HistoryNode(className);

        // First visited class
        if (head == null) {

            head = newNode;
            tail = newNode;
            current = newNode;

            return;
        }

        // Remove forward history if user previously moved back
        if (current != tail) {

            current.setNext(null);
            tail = current;
        }

        tail.setNext(newNode);
        newNode.setPrevious(tail);

        tail = newNode;
        current = newNode;
    }

    public String goBack() {

        if (current != null
                && current.getPrevious() != null) {

            current = current.getPrevious();
        }

        return getCurrentClass();
    }

    public String goForward() {

        if (current != null
                && current.getNext() != null) {

            current = current.getNext();
        }

        return getCurrentClass();
    }

    public String getCurrentClass() {

        if (current == null) {
            return null;
        }

        return current.getClassName();
    }

    public void clearHistory() {

        head = null;
        tail = null;
        current = null;
    }
}