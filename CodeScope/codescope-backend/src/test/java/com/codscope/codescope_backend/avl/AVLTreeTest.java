package com.codscope.codescope_backend.avl;

import com.codscope.codescope_backend.model.ClassInfo;

public class AVLTreeTest {

    public static void main(String[] args) {

        AVLTree tree = new AVLTree();

        tree.insert(new ClassInfo("BookingController", "com.codescope.controller", "BookingController.java"));
        tree.insert(new ClassInfo("BookingService", "com.codescope.service", "BookingService.java"));
        tree.insert(new ClassInfo("PaymentService", "com.codescope.service", "PaymentService.java"));
        tree.insert(new ClassInfo("DatabaseManager", "com.codescope.database", "DatabaseManager.java"));
        tree.insert(new ClassInfo("UserController", "com.codescope.controller", "UserController.java"));

        tree.displayInOrder();

        System.out.println();
        System.out.println("Height : " + tree.height());

        ClassInfo found = tree.search("PaymentService");

        System.out.println();
        System.out.println(
                "Search PaymentService : " +
                        (found == null ? "Not Found" : found.getFullClassName())
        );

        tree.delete("BookingController");

        System.out.println();
        System.out.println("After deleting BookingController :");

        tree.displayInOrder();
    }
}
