package com.codscope.codescope_backend.controller;

import com.codscope.codescope_backend.model.GraphData;
import com.codscope.codescope_backend.model.ImpactResult;
import com.codscope.codescope_backend.heap.HeapNode;
import com.codscope.codescope_backend.model.ClassInfo;
import com.codscope.codescope_backend.service.CodeScopeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/codescope")
@CrossOrigin(origins = "*")
public class CodescopeController {

    private final CodeScopeService codeScopeService;

    public CodescopeController(CodeScopeService codeScopeService) {
        this.codeScopeService = codeScopeService;
    }

    // Scan a Java project
    @PostMapping("/scan")
    public ResponseEntity<String> scanProject(
            @RequestParam String projectPath
    ) {

        try {

            codeScopeService.scanProject(projectPath);

            if (!codeScopeService.hasLoadedProject()) {

                return ResponseEntity
                        .badRequest()
                        .body("No Java classes were found.");
            }

            return ResponseEntity.ok(
                    "Project scanned successfully. Classes found: "
                            + codeScopeService.getClassCount()
            );

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .badRequest()
                    .body(exception.getMessage());

        } catch (Exception exception) {

            return ResponseEntity
                    .internalServerError()
                    .body(
                            "Project scanning failed: "
                                    + exception.getMessage()
                    );
        }
    }

    // Return scanned class
    @GetMapping("/classes")
    public ResponseEntity<List<ClassInfo>> getAllClasses() {

        if (!codeScopeService.hasLoadedProject()) {

            return ResponseEntity
                    .noContent()
                    .build();
        }

        return ResponseEntity.ok(
                codeScopeService.getScannedClasses()
        );
    }

    @GetMapping("/graph")
    public ResponseEntity<GraphData> getGraphData() {

        if (!codeScopeService.hasLoadedProject()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                codeScopeService.getGraphData()
        );
    }
    @GetMapping("/graph/dfs/{className}")
    public ResponseEntity<List<ClassInfo>> runDFS(
            @PathVariable String className
    ) {

        List<ClassInfo> result =
                codeScopeService.getDFSResult(className);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/graph/bfs/{className}")
    public ResponseEntity<List<ClassInfo>> runBFS(
            @PathVariable String className
    ) {

        List<ClassInfo> result =
                codeScopeService.getBFSResult(className);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/impact/{className}")
    public ResponseEntity<List<ImpactResult>>
    getImpactAnalysis(
            @PathVariable String className
    ) {

        if (codeScopeService
                .getGraph()
                .getNode(className) == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(
                codeScopeService
                        .getImpactAnalysis(className)
        );
    }
    // Search for a class using the AVL Tree
    @GetMapping("/classes/search/{className}")
    public ResponseEntity<ClassInfo> searchClass(
            @PathVariable String className
    ) {

        ClassInfo result =
                codeScopeService.searchClass(className);

        if (result == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(result);
    }

    // Highest-risk class from Max Heap
    @GetMapping("/risk/highest")
    public ResponseEntity<HeapNode> getHighestRiskClass() {

        HeapNode highestRisk =
                codeScopeService.getHighestRiskClass();

        if (highestRisk == null) {

            return ResponseEntity
                    .noContent()
                    .build();
        }

        return ResponseEntity.ok(highestRisk);
    }

    // Top risk classes from Max Heap
    @GetMapping("/risk/top")
    public ResponseEntity<List<HeapNode>> getTopRiskClasses(
            @RequestParam(defaultValue = "5") int count
    ) {

        if (count <= 0) {

            return ResponseEntity
                    .badRequest()
                    .build();
        }

        List<HeapNode> result =
                codeScopeService.getTopRiskClasses(count);

        return ResponseEntity.ok(result);
    }

    // Return current navigation class
    @GetMapping("/history/current")
    public ResponseEntity<String> getCurrentClass() {

        String current =
                codeScopeService.getCurrentClass();

        if (current == null) {

            return ResponseEntity
                    .noContent()
                    .build();
        }

        return ResponseEntity.ok(current);
    }

    // Navigate backward
    @PostMapping("/history/back")
    public ResponseEntity<String> goBack() {

        String current =
                codeScopeService.goBack();

        if (current == null) {

            return ResponseEntity
                    .noContent()
                    .build();
        }

        return ResponseEntity.ok(current);
    }

    // Navigate forward
    @PostMapping("/history/forward")
    public ResponseEntity<String> goForward() {

        String current =
                codeScopeService.goForward();

        if (current == null) {

            return ResponseEntity
                    .noContent()
                    .build();
        }

        return ResponseEntity.ok(current);
    }

    // Clear navigation history
    @DeleteMapping("/history")
    public ResponseEntity<String> clearHistory() {

        codeScopeService.clearNavigationHistory();

        return ResponseEntity.ok(
                "Navigation history cleared."
        );
    }
}