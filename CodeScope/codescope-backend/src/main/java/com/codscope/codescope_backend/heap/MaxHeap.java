package com.codscope.codescope_backend.heap;

import com.codscope.codescope_backend.model.ClassInfo;

import java.util.ArrayList;
import java.util.List;

public class MaxHeap {

    private final List<HeapNode> heap;

    public MaxHeap() {
        heap = new ArrayList<>();
    }

    // Insert class according to risk score
    public void insert(
            ClassInfo classInfo,
            double riskScore
    ) {

        if (containsClass(classInfo)) {

            throw new IllegalArgumentException(
                    "Class already exists in the heap: "
                            + classInfo.getClassName()
            );
        }

        HeapNode newNode =
                new HeapNode(
                        classInfo,
                        riskScore
                );

        heap.add(newNode);

        heapifyUp(
                heap.size() - 1
        );
    }

    // Return highest-risk class
    public HeapNode peekMax() {

        if (isEmpty()) {
            throw new IllegalStateException(
                    "Heap is empty."
            );
        }

        return heap.get(0);
    }

    // Remove and return highest-risk class
    private HeapNode extractMax() {

        if (isEmpty()) {
            throw new IllegalStateException(
                    "Heap is empty."
            );
        }

        HeapNode maxNode =
                heap.get(0);

        int lastIndex =
                heap.size() - 1;

        HeapNode lastNode =
                heap.remove(lastIndex);

        if (!heap.isEmpty()) {

            heap.set(
                    0,
                    lastNode
            );

            heapifyDown(0);
        }

        return maxNode;
    }

    // Return top risky classes without changing original heap
    public List<HeapNode> getTopRiskClasses(
            int count
    ) {

        if (count <= 0) {

            throw new IllegalArgumentException(
                    "Count must be greater than zero."
            );
        }

        MaxHeap temporaryHeap =
                new MaxHeap();

        for (HeapNode node : heap) {

            temporaryHeap.insert(
                    node.getClassInfo(),
                    node.getRiskScore()
            );
        }

        List<HeapNode> topClasses =
                new ArrayList<>();

        int limit =
                Math.min(
                        count,
                        temporaryHeap.heap.size()
                );

        for (int i = 0; i < limit; i++) {

            topClasses.add(
                    temporaryHeap.extractMax()
            );
        }

        return topClasses;
    }

    // Move node upward after insertion
    private void heapifyUp(
            int currentIndex
    ) {

        while (currentIndex > 0) {

            int parentIndex =
                    (currentIndex - 1) / 2;

            double currentRisk =
                    heap.get(currentIndex)
                            .getRiskScore();

            double parentRisk =
                    heap.get(parentIndex)
                            .getRiskScore();

            if (currentRisk <= parentRisk) {
                break;
            }

            swap(
                    currentIndex,
                    parentIndex
            );

            currentIndex =
                    parentIndex;
        }
    }

    // Restore heap after removing root
    private void heapifyDown(
            int currentIndex
    ) {

        while (true) {

            int leftChildIndex =
                    (2 * currentIndex) + 1;

            int rightChildIndex =
                    (2 * currentIndex) + 2;

            int largestIndex =
                    currentIndex;

            if (leftChildIndex < heap.size()
                    && heap.get(leftChildIndex)
                    .getRiskScore()
                    > heap.get(largestIndex)
                    .getRiskScore()) {

                largestIndex =
                        leftChildIndex;
            }

            if (rightChildIndex < heap.size()
                    && heap.get(rightChildIndex)
                    .getRiskScore()
                    > heap.get(largestIndex)
                    .getRiskScore()) {

                largestIndex =
                        rightChildIndex;
            }

            if (largestIndex == currentIndex) {
                break;
            }

            swap(
                    currentIndex,
                    largestIndex
            );

            currentIndex =
                    largestIndex;
        }
    }

    private void swap(
            int firstIndex,
            int secondIndex
    ) {

        HeapNode temporary =
                heap.get(firstIndex);

        heap.set(
                firstIndex,
                heap.get(secondIndex)
        );

        heap.set(
                secondIndex,
                temporary
        );
    }

    // Prevent duplicate classes
    private boolean containsClass(
            ClassInfo classInfo
    ) {

        for (HeapNode node : heap) {

            ClassInfo existingClass =
                    node.getClassInfo();

            boolean sameClassName =
                    existingClass
                            .getClassName()
                            .equals(
                                    classInfo.getClassName()
                            );

            boolean samePackage =
                    existingClass
                            .getPackageName()
                            .equals(
                                    classInfo.getPackageName()
                            );

            if (sameClassName
                    && samePackage) {

                return true;
            }
        }

        return false;
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }
}