package com.codscope.codescope_backend.avl;

import com.codscope.codescope_backend.model.ClassInfo;

public class AVLNode {

    private final ClassInfo classInfo;
    private AVLNode left;
    private AVLNode right;
    private int height;

    public AVLNode(ClassInfo classInfo) {
        this.classInfo = classInfo;
        this.left = null;
        this.right = null;
        this.height = 1;
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public AVLNode getLeft() {
        return left;
    }

    public void setLeft(AVLNode left) {
        this.left = left;
    }

    public AVLNode getRight() {
        return right;
    }

    public void setRight(AVLNode right) {
        this.right = right;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return classInfo.getClassName();
    }
}
