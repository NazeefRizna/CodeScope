package com.codscope.codescope_backend.heap;

import com.codscope.codescope_backend.model.ClassInfo;

public class HeapNode {

    private ClassInfo classInfo;
    private double riskScore;

    public HeapNode(ClassInfo classInfo, double riskScore){

        if (classInfo == null){

            throw new IllegalArgumentException(" class information cannot be null.");

        }

        if (classInfo.getClassName() == null || classInfo.getClasssName().isBlank()){

            throw new IllegalArgumentException (" class name cannot be empty. ");

        }

        validateRiskScore(riskScore);

        this.classInfo= classInfo;
        this.riskScore= riskScore;
    }

    public ClassInfo getClasInfo(){

        return classInfo;
    }

    public double getRiskScore(){

        return riskScore;
    }

    public void setRiskScore(double riskScore){

        validateRiskScore(riskScore);
        this.riskScore = riskScore;

    }

    prvate void validateRiskScore(double riskScore){

        if(riskScore < 0 || double.isNaN(riskScore) || double.isfinite(riskScore) ){

            throw new IllegalArgumentException("Risk score must be a valid non-negative value. ");

        }

    }

    @Overide
    public String toString(){
        return classInfo.getClassName() + "_CRI" + riskScore;
    }
}
