package com.codscope.codescope_backend.heap;

import com.codscope.codescope_backend.model.ClassInfo;

public class HeapNode {

    private ClassInfo classInfo;
    private double riskScore;

    public HeapNode(ClassInfo classInfo, double riskScore){
        this.classInfo= classInfo;
        this.riskScore= riskScore;
    }

    public ClassInfo getClasInfo(){
        return classInfo;
    }

    public double getRiskScore(){
        return riskScore;
    }
}
