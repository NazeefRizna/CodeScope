package com.codscope.codescope_backend.avl;

import com.codscope.codescope_backend.model.ClassInfo;

public class AVLTree {

    private AVLNode root;

    public AVLTree() {
        root = null;
    }

    public void insert(ClassInfo classInfo) {
        if (classInfo == null) {
            return;
        }

        root = insertNode(root, classInfo);
    }

    private AVLNode insertNode(AVLNode node, ClassInfo classInfo) {

        if (node == null) {
            return new AVLNode(classInfo);
        }

        int comparison = classInfo.getClassName()
                .compareTo(node.getClassInfo().getClassName());

        if (comparison < 0) {
            node.setLeft(insertNode(node.getLeft(), classInfo));

        } else if (comparison > 0) {
            node.setRight(insertNode(node.getRight(), classInfo));

        } else {
            // Ignore duplicate class names
            return node;
        }

        updateHeight(node);
        return balance(node);
    }

    public ClassInfo search(String className) {

        if (className == null || className.isBlank()) {
            return null;
        }

        AVLNode result = searchNode(root, className);

        return result == null ? null : result.getClassInfo();
    }

    private AVLNode searchNode(AVLNode node, String className) {

        if (node == null) {
            return null;
        }

        int comparison = className.compareTo(
                node.getClassInfo().getClassName()
        );

        if (comparison == 0) {
            return node;

        } else if (comparison < 0) {
            return searchNode(node.getLeft(), className);

        } else {
            return searchNode(node.getRight(), className);
        }
    }

    public void delete(String className) {

        if (className == null || className.isBlank()) {
            return;
        }

        root = deleteNode(root, className);
    }

    private AVLNode deleteNode(AVLNode node, String className) {

        if (node == null) {
            return null;
        }

        int comparison = className.compareTo(
                node.getClassInfo().getClassName()
        );

        if (comparison < 0) {
            node.setLeft(deleteNode(node.getLeft(), className));

        } else if (comparison > 0) {
            node.setRight(deleteNode(node.getRight(), className));

        } else {

            // Return remaining child when node has only one child
            if (node.getLeft() == null) {
                return node.getRight();
            }

            if (node.getRight() == null) {
                return node.getLeft();
            }

            // Replace node with the smallest node from right subtree
            AVLNode successor = findMinimum(node.getRight());

            AVLNode replacement = new AVLNode(
                    successor.getClassInfo()
            );

            replacement.setLeft(node.getLeft());

            replacement.setRight(
                    deleteNode(
                            node.getRight(),
                            successor.getClassInfo().getClassName()
                    )
            );

            updateHeight(replacement);

            return balance(replacement);
        }

        updateHeight(node);

        return balance(node);
    }

    private AVLNode findMinimum(AVLNode node) {

        AVLNode current = node;

        while (current.getLeft() != null) {
            current = current.getLeft();
        }

        return current;
    }

    public int height() {
        return getHeight(root);
    }

    private int getHeight(AVLNode node) {
        return node == null ? 0 : node.getHeight();
    }

    private void updateHeight(AVLNode node) {

        if (node == null) {
            return;
        }

        node.setHeight(
                1 + Math.max(
                        getHeight(node.getLeft()),
                        getHeight(node.getRight())
                )
        );
    }

    public void displayInOrder() {
        displayInOrder(root);
    }

    private void displayInOrder(AVLNode node) {

        if (node == null) {
            return;
        }

        displayInOrder(node.getLeft());

        System.out.println(
                node.getClassInfo().getFullClassName()
        );

        displayInOrder(node.getRight());
    }

    private int getBalanceFactor(AVLNode node) {

        if (node == null) {
            return 0;
        }

        return getHeight(node.getLeft())
                - getHeight(node.getRight());
    }

    private AVLNode balance(AVLNode node) {

        if (node == null) {
            return null;
        }

        int balanceFactor = getBalanceFactor(node);

        if (balanceFactor > 1) {

            // Left-Right case
            if (getBalanceFactor(node.getLeft()) < 0) {
                node.setLeft(rotateLeft(node.getLeft()));
            }

            return rotateRight(node);
        }

        if (balanceFactor < -1) {

            // Right-Left case
            if (getBalanceFactor(node.getRight()) > 0) {
                node.setRight(rotateRight(node.getRight()));
            }

            return rotateLeft(node);
        }

        return node;
    }

    private AVLNode rotateRight(AVLNode node) {

        AVLNode newRoot = node.getLeft();
        AVLNode transferSubtree = newRoot.getRight();

        newRoot.setRight(node);
        node.setLeft(transferSubtree);

        // Update lower node before new root
        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    private AVLNode rotateLeft(AVLNode node) {

        AVLNode newRoot = node.getRight();
        AVLNode transferSubtree = newRoot.getLeft();

        newRoot.setLeft(node);
        node.setRight(transferSubtree);

        // Update lower node before new root
        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }
}