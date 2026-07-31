package com.codscope.codescope_backend.heap;

import com.codscope.codescope_backend.model.ClassInfo;

import java.util.ArrayList;
import java.util.List;

public class MaxHeap {

    private final List<HeapNode> heap;

    public MaxHeap()
    {
        heap = new ArrayList<>();
    }

    //Add a new class to the Max Heap.
    public void insert(ClassInfo classInfo, double riskScore) {

        HeapNode newNode = new HeapNode(classInfo, riskScore);

        if (containsClass(classInfo))
        {
            throw new IllegalArgumentException("Class already exists in the heap: " + classInfo.getClassName());
        }

        heap.add(newNode);
        int currentIndex = heap.size() - 1;
        heapifyUp(currentIndex);
    }

    //Return the highest risk class without removing it.
    public HeapNode peekMax() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty.");
        }

        return heap.get(0);
    }


    //Remove and return the highest risk class.
    public HeapNode extractMax() {

        if (isEmpty())
        {
            throw new IllegalStateException("Heap is empty.");
        }

        HeapNode maxNode = heap.get(0);
        int lastIndex = heap.size() - 1;

        HeapNode lastNode = heap.remove(lastIndex);

        if (!heap.isEmpty())
        {

            heap.set(0, lastNode);
            heapifyDown(0);
        }

        return maxNode;
    }


    //Return the top risky classes without changing heap
    public List<HeapNode> getTopRiskClasses(int count)
    {

        if (count <= 0)
        {
            throw new IllegalArgumentException("Count must be greater than zero.");
        }

        MaxHeap temporaryHeap = new MaxHeap();

        for (HeapNode node : heap) {

            temporaryHeap.insert(node.getClassInfo(), node.getRiskScore());
        }

        List<HeapNode> topClasses = new ArrayList<>();

        int limit = Math.min(count,temporaryHeap.size());

        for (int i = 0; i < limit; i++)
        {
            topClasses.add(temporaryHeap.extractMax());
        }

        return topClasses;
    }


    //Move a newly inserted node upwards.
    private void heapifyUp(int currentIndex)
    {
        while (currentIndex > 0)
        {
            int parentIndex = (currentIndex - 1) / 2;
            double currentRisk = heap.get(currentIndex).getRiskScore();

            double parentRisk = heap.get(parentIndex).getRiskScore();

            if (currentRisk <= parentRisk)
            {
                break;
            }

            swap(currentIndex, parentIndex);
            currentIndex = parentIndex;
        }
    }


    //Move the root node downwards after removing max
    private void heapifyDown(int currentIndex) {
        while (true)
        {
            int leftChildIndex = (2 * currentIndex) + 1;

            int rightChildIndex = (2 * currentIndex) + 2;

            int largestIndex = currentIndex;

            if (leftChildIndex < heap.size() && heap.get(leftChildIndex).getRiskScore() > heap.get(largestIndex).getRiskScore())
            {
                largestIndex = leftChildIndex;
            }

            if (rightChildIndex < heap.size() && heap.get(rightChildIndex).getRiskScore() > heap.get(largestIndex).getRiskScore())
            {
                largestIndex = rightChildIndex;
            }

            if (largestIndex == currentIndex)
            {
                break;
            }

            swap(currentIndex, largestIndex);
            currentIndex = largestIndex;
        }
    }

    private void swap(int firstIndex, int secondIndex) {

        HeapNode temporary = heap.get(firstIndex);
        heap.set(firstIndex, heap.get(secondIndex));

        heap.set(secondIndex, temporary);
    }


    //Prevent duplicate classes from being inserted.
    private boolean containsClass(ClassInfo classInfo) {

        for (HeapNode node : heap) {

            ClassInfo existingClass = node.getClassInfo();

            boolean sameClassName = existingClass.getClassName().equals(classInfo.getClassName());

            boolean samePackage = existingClass.getPackageName().equals(classInfo.getPackageName());

            if (sameClassName && samePackage)
            {
                return true;
            }
        }

        return false;
    }

    public boolean isEmpty()
    {
        return heap.isEmpty();
    }

    public int size()
    {
        return heap.size();
    }

    public void clear()
    {
        heap.clear();
    }


    //Shows the internal Max Heap arrangement.
    public void displayHeap() {

        if (isEmpty())
        {
            System.out.println("Heap is empty.");
            return;
        }

        System.out.println("\n----Max Heap----"
        );

        for (int i = 0; i < heap.size(); i++)
        {
            System.out.println("Index " + i + " : " + heap.get(i));
        }
    }

    //Shows classes from highest risk to lowest risk.
    public void displayTopRiskClasses(int count)
    {
        List<HeapNode> topClasses = getTopRiskClasses(count);

        System.out.println("\n----TOP RISK CLASSES----");

        for (int i = 0; i < topClasses.size(); i++)
        {
            System.out.println((i + 1) + ". " + topClasses.get(i));
        }
    }
}