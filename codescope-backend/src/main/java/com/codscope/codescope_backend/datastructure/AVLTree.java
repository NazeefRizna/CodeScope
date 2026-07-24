package com.codscope.codescope_backend.datastructure;

import com.codscope.codescope_backend.model.ClassInfo;
import java.util.ArrayList;
import java.util.List;

public class AVLTree {
    private AVLNode root;

    public AVLTree() {
        this.root = null;
    }

    // Get height of a node
    private int getHeight(AVLNode node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    // Get balance factor
    private int getBalanceFactor(AVLNode node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }

    // Update height of a node
    private void updateHeight(AVLNode node) {
        if (node == null) {
            return;
        }
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
    }

    // Right rotation
    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    // Left rotation
    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    // Insert a ClassInfo into the AVL Tree
    public void insert(ClassInfo classInfo) {
        if (classInfo == null || classInfo.getClassName() == null) {
            System.out.println("Cannot insert null ClassInfo or null className");
            return;
        }
        root = insertRecursive(root, classInfo);
    }

    private AVLNode insertRecursive(AVLNode node, ClassInfo classInfo) {
        // Normal BST insertion
        if (node == null) {
            System.out.println("Inserted into AVL Tree: " + classInfo.getClassName());
            return new AVLNode(classInfo);
        }

        int comparison = classInfo.getClassName().compareTo(node.classInfo.getClassName());

        if (comparison < 0) {
            node.left = insertRecursive(node.left, classInfo);
        } else if (comparison > 0) {
            node.right = insertRecursive(node.right, classInfo);
        } else {
            // Duplicate - update existing node
            System.out.println("Class already exists, updating: " + classInfo.getClassName());
            node.classInfo = classInfo;
            return node;
        }

        // Update height
        updateHeight(node);

        // Get balance factor
        int balanceFactor = getBalanceFactor(node);

        // Left Left Case
        if (balanceFactor > 1 && getBalanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }

        // Right Right Case
        if (balanceFactor < -1 && getBalanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }

        // Left Right Case
        if (balanceFactor > 1 && getBalanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Left Case
        if (balanceFactor < -1 && getBalanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    // Search for a ClassInfo by className - O(log n)
    public ClassInfo search(String className) {
        if (className == null) {
            return null;
        }
        return searchRecursive(root, className);
    }

    private ClassInfo searchRecursive(AVLNode node, String className) {
        if (node == null) {
            return null;
        }

        int comparison = className.compareTo(node.classInfo.getClassName());

        if (comparison < 0) {
            return searchRecursive(node.left, className);
        } else if (comparison > 0) {
            return searchRecursive(node.right, className);
        } else {
            return node.classInfo;
        }
    }

    // Get minimum node
    private AVLNode getMinNode(AVLNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Delete a ClassInfo by className
    public void delete(String className) {
        if (className == null) {
            return;
        }
        root = deleteRecursive(root, className);
    }

    private AVLNode deleteRecursive(AVLNode node, String className) {
        if (node == null) {
            return null;
        }

        int comparison = className.compareTo(node.classInfo.getClassName());

        if (comparison < 0) {
            node.left = deleteRecursive(node.left, className);
        } else if (comparison > 0) {
            node.right = deleteRecursive(node.right, className);
        } else {
            // Node found
            System.out.println("Deleted from AVL Tree: " + className);

            // Case 1: No children (leaf node)
            if (node.left == null && node.right == null) {
                return null;
            }

            // Case 2: One child
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }

            // Case 3: Two children
            AVLNode minNode = getMinNode(node.right);
            node.classInfo = minNode.classInfo;
            node.right = deleteRecursive(node.right, minNode.classInfo.getClassName());
        }

        // Update height and rebalance
        if (node == null) {
            return null;
        }

        updateHeight(node);

        int balanceFactor = getBalanceFactor(node);

        // Left Left Case
        if (balanceFactor > 1 && getBalanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }

        // Right Right Case
        if (balanceFactor < -1 && getBalanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }

        // Left Right Case
        if (balanceFactor > 1 && getBalanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Left Case
        if (balanceFactor < -1 && getBalanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    // Get all ClassInfo objects in sorted order (in-order traversal)
    public List<ClassInfo> getAllClassesInOrder() {
        List<ClassInfo> result = new ArrayList<>();
        inOrderTraversal(root, result);
        return result;
    }

    private void inOrderTraversal(AVLNode node, List<ClassInfo> result) {
        if (node == null) {
            return;
        }
        inOrderTraversal(node.left, result);
        result.add(node.classInfo);
        inOrderTraversal(node.right, result);
    }

    // Display tree structure (for debugging)
    public void displayTree() {
        if (root == null) {
            System.out.println("Tree is empty");
            return;
        }
        displayTreeRecursive(root, "", true);
    }

    private void displayTreeRecursive(AVLNode node, String prefix, boolean isTail) {
        if (node == null) {
            return;
        }

        System.out.println(prefix + (isTail ? "└── " : "├── ") + 
                           node.classInfo.getClassName() + 
                           " (h:" + node.height + ", bf:" + getBalanceFactor(node) + ")");

        if (node.left != null || node.right != null) {
            if (node.left != null) {
                displayTreeRecursive(node.left, 
                                   prefix + (isTail ? "    " : "│   "), 
                                   node.right == null);
            }
            if (node.right != null) {
                displayTreeRecursive(node.right, 
                                   prefix + (isTail ? "    " : "│   "), 
                                   true);
            }
        }
    }

    // Check if tree contains a class
    public boolean contains(String className) {
        return search(className) != null;
    }

    // Get total number of nodes
    public int size() {
        return getAllClassesInOrder().size();
    }

    // Check if tree is empty
    public boolean isEmpty() {
        return root == null;
    }

    // Clear the tree
    public void clear() {
        root = null;
    }

    // Get tree height
    public int getTreeHeight() {
        return getHeight(root);
    }

    // Search for classes by prefix (useful for autocomplete)
    public List<ClassInfo> searchByPrefix(String prefix) {
        List<ClassInfo> result = new ArrayList<>();
        searchByPrefixRecursive(root, prefix, result);
        return result;
    }

    private void searchByPrefixRecursive(AVLNode node, String prefix, List<ClassInfo> result) {
        if (node == null) {
            return;
        }

        if (node.classInfo.getClassName().startsWith(prefix)) {
            result.add(node.classInfo);
        }

        // Continue searching both subtrees since we need all matches
        searchByPrefixRecursive(node.left, prefix, result);
        searchByPrefixRecursive(node.right, prefix, result);
    }
}
