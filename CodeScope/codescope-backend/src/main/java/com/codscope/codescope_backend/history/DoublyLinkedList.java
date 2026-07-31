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

    // Visit a class
    public void visitClass(String className) {

        HistoryNode newNode = new HistoryNode(className);

        // First class visited
        if (head == null) {
            head = newNode;
            tail = newNode;
            current = newNode;
            size++;
            return;
        }

        // If user went back, remove forward history
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

    // Go back
    public void goBack() {

        if (current != null && current.getPrevious() != null) {

            current = current.getPrevious();

            System.out.println(
                    "Moved Back -> " + current.getClassName()
            );

        } else {

            System.out.println("Already at first visited class.");

        }

    }

    // Go forward
    public void goForward() {

        if (current != null && current.getNext() != null) {

            current = current.getNext();

            System.out.println(
                    "Moved Forward -> " + current.getClassName()
            );

        } else {

            System.out.println("Already at latest visited class.");

        }

    }

    // Current class
    public String getCurrentClass() {

        if (current == null) {
            return "No class selected.";
        }

        return current.getClassName();

    }

    // Display history
    public void displayHistory() {

        if (head == null) {

            System.out.println("History is empty.");
            return;

        }

        System.out.println("\nNavigation History");

        HistoryNode temp = head;

        while (temp != null) {

            if (temp == current) {

                System.out.println(
                        temp.getClassName() + "   <-- Current"
                );

            } else {

                System.out.println(temp.getClassName());

            }

            temp = temp.getNext();
        }

    }

    // Clear history
    public void clearHistory() {

        head = null;
        tail = null;
        current = null;
        size = 0;

        System.out.println("History cleared.");

    }

    // Check if empty
    public boolean isEmpty() {

        return head == null;

    }

    // Number of visited classes
    public int getSize() {

        return size;

    }

}