package com.codscope.codescope_backend;

import com.codscope.codescope_backend.model.ClassInfo;
import com.codscope.codescope_backend.scanner.JavaCodeScanner;
import com.codscope.codescope_backend.datastructure.AVLTree;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class CodescopeBackendApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CodescopeBackendApplication.class, args);
    }

    @Override
    public void run(String... args) {

        String projectPath = "C:/Users/nazee/Desktop/Co";

        JavaCodeScanner scanner = new JavaCodeScanner();

        System.out.println("\nScanning project: " + projectPath);

        List<ClassInfo> classes = scanner.scanProject(projectPath);

        System.out.println("\n ---CLASSES FOUND---\n");

        if (classes.isEmpty()) {
            System.out.println("No Java classes were found.");
            return;
        }

        for (ClassInfo classInfo : classes) {

            System.out.println("Class      : " + classInfo.getClassName());
            System.out.println("Package    : " + classInfo.getPackageName());
            System.out.println("File Path  : " + classInfo.getFilePath());

            System.out.println("-----------------------------------");
        }

        System.out.println("\nTotal Classes Found : " + classes.size());

         System.out.println("\n\n===== BUILDING AVL TREE =====\n");

        AVLTree avlTree = new AVLTree();

         for (ClassInfo classInfo : classes) {
            avlTree.insert(classInfo);
        }

        System.out.println("\nAVL Tree Size: " + avlTree.size());
        System.out.println("AVL Tree Height: " + avlTree.getTreeHeight());

        // Test searching
        System.out.println("\n===== SEARCHING IN AVL TREE =====\n");
        
        if (!classes.isEmpty()) {
            String firstClassName = classes.get(0).getClassName();
            System.out.println("Searching for: " + firstClassName);
            ClassInfo found = avlTree.search(firstClassName);
            if (found != null) {
                System.out.println("Found: " + found.getClassName() + " in package " + found.getPackageName());
            }
        }

         System.out.println("\n===== ALL CLASSES IN ALPHABETICAL ORDER =====\n");
        List<ClassInfo> orderedClasses = avlTree.getAllClassesInOrder();
        for (ClassInfo classInfo : orderedClasses) {
            System.out.println(classInfo.getClassName());
        }

         System.out.println("\n===== AVL TREE STRUCTURE =====\n");
        avlTree.displayTree();

         System.out.println("\n===== PREFIX SEARCH =====\n");
        if (!classes.isEmpty()) {
            String firstClassName = classes.get(0).getClassName();
            String prefix = firstClassName.substring(0, Math.min(3, firstClassName.length()));
            System.out.println("Searching for classes starting with: " + prefix);
            List<ClassInfo> prefixResults = avlTree.searchByPrefix(prefix);
            for (ClassInfo classInfo : prefixResults) {
                System.out.println("  - " + classInfo.getClassName());
            }
        }
    }
}
