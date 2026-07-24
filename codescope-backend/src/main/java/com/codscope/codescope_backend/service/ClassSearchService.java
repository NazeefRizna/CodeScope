package com.codscope.codescope_backend.service;

import com.codscope.codescope_backend.datastructure.AVLTree;
import com.codscope.codescope_backend.model.ClassInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassSearchService {
    
    private AVLTree avlTree;

    public ClassSearchService() {
        this.avlTree = new AVLTree();
    }

    /**
     * Build AVL Tree from list of ClassInfo objects
     * Time Complexity: O(n log n) for insertion
     */
    public void buildClassIndex(List<ClassInfo> classes) {
        avlTree.clear();
        
        System.out.println("Building AVL Tree index for " + classes.size() + " classes...");
        
        for (ClassInfo classInfo : classes) {
            avlTree.insert(classInfo);
        }
        
        System.out.println("AVL Tree built successfully!");
        System.out.println("  - Tree Height: " + avlTree.getTreeHeight());
        System.out.println("  - Total Classes: " + avlTree.size());
    }

    /**
     * Search for a specific class by name
     * Time Complexity: O(log n)
     */
    public ClassInfo searchClass(String className) {
        if (className == null || className.trim().isEmpty()) {
            return null;
        }
        return avlTree.search(className.trim());
    }

    /**
     * Search for classes by prefix (autocomplete)
     * Time Complexity: O(n) - needs to check all nodes, but tree structure helps
     */
    public List<ClassInfo> searchByPrefix(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            return avlTree.getAllClassesInOrder();
        }
        return avlTree.searchByPrefix(prefix.trim());
    }

    /**
     * Get all classes in alphabetical order
     * Time Complexity: O(n)
     */
    public List<ClassInfo> getAllClasses() {
        return avlTree.getAllClassesInOrder();
    }

    /**
     * Add a single class to the index
     * Time Complexity: O(log n)
     */
    public void addClass(ClassInfo classInfo) {
        avlTree.insert(classInfo);
    }

    /**
     * Remove a class from the index
     * Time Complexity: O(log n)
     */
    public void removeClass(String className) {
        avlTree.delete(className);
    }

    /**
     * Check if a class exists in the index
     * Time Complexity: O(log n)
     */
    public boolean classExists(String className) {
        return avlTree.contains(className);
    }

    /**
     * Get total number of indexed classes
     * Time Complexity: O(1) - size() caches or computes efficiently
     */
    public int getTotalClasses() {
        return avlTree.size();
    }

    /**
     * Get AVL Tree height (useful for analyzing balance)
     * Time Complexity: O(1)
     */
    public int getTreeHeight() {
        return avlTree.getTreeHeight();
    }

    /**
     * Check if index is empty
     * Time Complexity: O(1)
     */
    public boolean isEmpty() {
        return avlTree.isEmpty();
    }

    /**
     * Clear the entire index
     * Time Complexity: O(1)
     */
    public void clear() {
        avlTree.clear();
    }

    /**
     * Display tree structure (for debugging)
     */
    public void displayTreeStructure() {
        avlTree.displayTree();
    }
}
