package com.codscope.codescope_backend.history;

public class HistoryNode {

    private String className;
    private HistoryNode previous;
    private HistoryNode next;

    public HistoryNode(String className) {
        this.className = className;
        this.previous = null;
        this.next = null;
    }

    public String getClassName() {
        return className;
    }

    public HistoryNode getPrevious() {
        return previous;
    }

    public void setPrevious(HistoryNode previous) {
        this.previous = previous;
    }

    public HistoryNode getNext() {
        return next;
    }

    public void setNext(HistoryNode next) {
        this.next = next;
    }
}