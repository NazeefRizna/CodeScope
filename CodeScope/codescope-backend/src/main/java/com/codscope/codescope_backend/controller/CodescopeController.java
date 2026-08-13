package com.codscope.codescope_backend.controller;

import com.codscope.codescope_backend.heap.HeapNode;
import com.codscope.codescope_backend.model.ClassInfo;
import com.codscope.codescope_backend.model.GraphData;
import com.codscope.codescope_backend.model.ImpactResult;
import com.codscope.codescope_backend.service.CodeScopeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/codescope")
@CrossOrigin(origins = "*")
public class CodescopeController {

    private final CodeScopeService codeScopeService;

    public CodescopeController(
            CodeScopeService codeScopeService
    ) {

        this.codeScopeService =
                codeScopeService;
    }

    // Project Scanner
    @PostMapping("/scan")
    public ResponseEntity<String> scanProject(
            @RequestParam String projectPath
    ) {

        try {

            codeScopeService.scanProject(
                    projectPath
            );

            if (!codeScopeService.hasLoadedProject()) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "No Java classes were found."
                        );
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

    // Classes
    @GetMapping("/classes")
    public ResponseEntity<List<ClassInfo>>
    getAllClasses() {

        if (!codeScopeService.hasLoadedProject()) {

            return ResponseEntity
                    .noContent()
                    .build();
        }

        return ResponseEntity.ok(
                codeScopeService.getScannedClasses()
        );
    }

    // AVL Tree search
    @GetMapping("/classes/search/{className}")
    public ResponseEntity<ClassInfo> searchClass(
            @PathVariable String className
    ) {

        ClassInfo result =
                codeScopeService.searchClass(
                        className
                );

        if (result == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(result);
    }

    // Dependency Graph
    @GetMapping("/graph")
    public ResponseEntity<GraphData>
    getGraphData() {

        if (!codeScopeService.hasLoadedProject()) {

            return ResponseEntity
                    .noContent()
                    .build();
        }

        return ResponseEntity.ok(
                codeScopeService.getGraphData()
        );
    }

    // DFS - Trace dependencies
    @GetMapping("/graph/dfs/{className}")
    public ResponseEntity<List<ClassInfo>>
    traceDependencies(
            @PathVariable String className
    ) {

        if (!codeScopeService
                .graphContainsClass(className)) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(
                codeScopeService
                        .getDFSResult(className)
        );
    }

    // BFS - Shortest dependency path
    @GetMapping("/graph/shortest-path")
    public ResponseEntity<List<ClassInfo>>
    findShortestDependencyPath(
            @RequestParam String start,
            @RequestParam String target
    ) {

        if (!codeScopeService
                .graphContainsClass(start)
                || !codeScopeService
                .graphContainsClass(target)) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        List<ClassInfo> path =
                codeScopeService
                        .findShortestDependencyPath(
                                start,
                                target
                        );

        if (path.isEmpty()) {

            return ResponseEntity
                    .noContent()
                    .build();
        }

        return ResponseEntity.ok(path);
    }

    // Impact Analysis
    @GetMapping("/impact/{className}")
    public ResponseEntity<List<ImpactResult>>
    getImpactAnalysis(
            @PathVariable String className
    ) {

        if (!codeScopeService
                .graphContainsClass(className)) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        return ResponseEntity.ok(
                codeScopeService
                        .getImpactAnalysis(className)
        );
    }

    // Component Risk
    @GetMapping("/risk/highest")
    public ResponseEntity<HeapNode>
    getHighestRiskClass() {

        HeapNode highestRisk =
                codeScopeService
                        .getHighestRiskClass();

        if (highestRisk == null) {

            return ResponseEntity
                    .noContent()
                    .build();
        }

        return ResponseEntity.ok(
                highestRisk
        );
    }

    @GetMapping("/risk/top")
    public ResponseEntity<List<HeapNode>>
    getTopRiskClasses(
            @RequestParam(defaultValue = "5")
            int count
    ) {

        if (count <= 0) {

            return ResponseEntity
                    .badRequest()
                    .build();
        }

        return ResponseEntity.ok(
                codeScopeService
                        .getTopRiskClasses(count)
        );
    }

    // Navigation History
    @GetMapping("/history/current")
    public ResponseEntity<String>
    getCurrentClass() {

        String current =
                codeScopeService.getCurrentClass();

        if (current == null) {

            return ResponseEntity
                    .noContent()
                    .build();
        }

        return ResponseEntity.ok(current);
    }

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

    @DeleteMapping("/history")
    public ResponseEntity<String>
    clearHistory() {

        codeScopeService
                .clearNavigationHistory();

        return ResponseEntity.ok(
                "Navigation history cleared."
        );
    }
}