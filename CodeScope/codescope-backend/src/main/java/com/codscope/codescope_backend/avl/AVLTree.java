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

    private AVLNode insertNode(
            AVLNode node,
            ClassInfo classInfo
    ) {

        if (node == null) {
            return new AVLNode(classInfo);
        }

        int comparison =
                classInfo.getClassName()
                        .compareTo(
                                node.getClassInfo()
                                        .getClassName()
                        );

        if (comparison < 0) {

            node.setLeft(
                    insertNode(
                            node.getLeft(),
                            classInfo
                    )
            );

        } else if (comparison > 0) {

            node.setRight(
                    insertNode(
                            node.getRight(),
                            classInfo
                    )
            );

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

        AVLNode result =
                searchNode(root, className);

        return result == null
                ? null
                : result.getClassInfo();
    }

    private AVLNode searchNode(
            AVLNode node,
            String className
    ) {

        if (node == null) {
            return null;
        }

        int comparison =
                className.compareTo(
                        node.getClassInfo()
                                .getClassName()
                );

        if (comparison == 0) {

            return node;

        } else if (comparison < 0) {

            return searchNode(
                    node.getLeft(),
                    className
            );

        } else {

            return searchNode(
                    node.getRight(),
                    className
            );
        }
    }

    private void updateHeight(AVLNode node) {

        node.setHeight(
                1 + Math.max(
                        getHeight(node.getLeft()),
                        getHeight(node.getRight())
                )
        );
    }

    private int getHeight(AVLNode node) {

        return node == null
                ? 0
                : node.getHeight();
    }

    private int getBalanceFactor(
            AVLNode node
    ) {

        if (node == null) {
            return 0;
        }

        return getHeight(node.getLeft())
                - getHeight(node.getRight());
    }

    private AVLNode balance(AVLNode node) {

        int balanceFactor =
                getBalanceFactor(node);

        // Left heavy
        if (balanceFactor > 1) {

            // Left-Right case
            if (getBalanceFactor(
                    node.getLeft()) < 0) {

                node.setLeft(
                        rotateLeft(
                                node.getLeft()
                        )
                );
            }

            return rotateRight(node);
        }

        // Right heavy
        if (balanceFactor < -1) {

            // Right-Left case
            if (getBalanceFactor(
                    node.getRight()) > 0) {

                node.setRight(
                        rotateRight(
                                node.getRight()
                        )
                );
            }

            return rotateLeft(node);
        }

        return node;
    }

    private AVLNode rotateRight(
            AVLNode node
    ) {

        AVLNode newRoot =
                node.getLeft();

        AVLNode transferSubtree =
                newRoot.getRight();

        newRoot.setRight(node);
        node.setLeft(transferSubtree);

        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    private AVLNode rotateLeft(
            AVLNode node
    ) {

        AVLNode newRoot =
                node.getRight();

        AVLNode transferSubtree =
                newRoot.getLeft();

        newRoot.setLeft(node);
        node.setRight(transferSubtree);

        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }
}