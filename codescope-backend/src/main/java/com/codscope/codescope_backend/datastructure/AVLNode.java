package com.codscope.codescope_backend.datastructure;

import com.codscope.codescope_backend.model.ClassInfo;

public class AVLNode {
    public ClassInfo classInfo;
    public AVLNode left;
    public AVLNode right;
    public int height;

    public AVLNode(ClassInfo classInfo) {
        this.classInfo = classInfo;
        this.left = null;
        this.right = null;
        this.height = 1;
    }
}
