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

        int comparison = classInfo.getFullClassName()
                .compareTo(node.getClassInfo().getFullClassName());

        if (comparison < 0) {
            node.setLeft(insertNode(node.getLeft(), classInfo));
        } else if (comparison > 0) {
            node.setRight(insertNode(node.getRight(), classInfo));
        } else {
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

        if (node == null || className == null) {
            return null;
        }

        int comparison = className.compareTo(node.getClassInfo().getFullClassName());

        if (comparison == 0) {
            return node;
        } else if (comparison < 0) {
            return searchNode(node.getLeft(), className);
        } else {
            return searchNode(node.getRight(), className);
        }
    }

    public boolean contains(String className) {
        return search(className) != null;
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

        int comparison = className.compareTo(node.getClassInfo().getFullClassName());

        if (comparison < 0) {
            node.setLeft(deleteNode(node.getLeft(), className));
        } else if (comparison > 0) {
            node.setRight(deleteNode(node.getRight(), className));
        } else {

            if (node.getLeft() == null) {
                return node.getRight();
            } else if (node.getRight() == null) {
                return node.getLeft();
            }

            AVLNode successor = getMinNode(node.getRight());

            AVLNode replacement = new AVLNode(successor.getClassInfo());
            replacement.setLeft(node.getLeft());
            replacement.setRight(
                    deleteNode(node.getRight(), successor.getClassInfo().getFullClassName())
            );

            node = replacement;
        }

        updateHeight(node);

        return balance(node);
    }

    private AVLNode getMinNode(AVLNode node) {

        while (node.getLeft() != null) {
            node = node.getLeft();
        }

        return node;
    }

    private void updateHeight(AVLNode node) {
        node.setHeight(1 + Math.max(getHeight(node.getLeft()), getHeight(node.getRight())));
    }

    private int getHeight(AVLNode node) {
        return node == null ? 0 : node.getHeight();
    }

    private int getBalanceFactor(AVLNode node) {
        return node == null ? 0 : getHeight(node.getLeft()) - getHeight(node.getRight());
    }

    private AVLNode balance(AVLNode node) {

        int balanceFactor = getBalanceFactor(node);

        if (balanceFactor > 1) {

            if (getBalanceFactor(node.getLeft()) < 0) {
                node.setLeft(rotateLeft(node.getLeft()));
            }

            return rotateRight(node);
        }

        if (balanceFactor < -1) {

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

        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    private AVLNode rotateLeft(AVLNode node) {

        AVLNode newRoot = node.getRight();
        AVLNode transferSubtree = newRoot.getLeft();

        newRoot.setLeft(node);
        node.setRight(transferSubtree);

        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    public void displayInOrder() {

        System.out.println("\n----- AVL TREE (SORTED BY CLASS NAME) -----\n");

        inOrder(root);
    }

    private void inOrder(AVLNode node) {

        if (node == null) {
            return;
        }

        inOrder(node.getLeft());

        System.out.println(
                node.getClassInfo().getClassName() +
                        "  (" + node.getClassInfo().getPackageName() + ")"
        );

        inOrder(node.getRight());
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int height() {
        return getHeight(root);
    }
}