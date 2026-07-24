package com.codscope.codescope_backend.controller;

import com.codscope.codescope_backend.model.ClassInfo;
import com.codscope.codescope_backend.service.ClassSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class ClassSearchController {

    @Autowired
    private ClassSearchService classSearchService;

    /**
     * Search for a specific class by name
     * GET /api/search/class?name=BookingService
     */
    @GetMapping("/class")
    public ResponseEntity<?> searchClass(@RequestParam String name) {
        ClassInfo result = classSearchService.searchClass(name);
        
        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.ok("Class '" + name + "' not found");
        }
    }

    /**
     * Search for classes by prefix (autocomplete)
     * GET /api/search/prefix?prefix=Book
     */
    @GetMapping("/prefix")
    public ResponseEntity<List<ClassInfo>> searchByPrefix(@RequestParam String prefix) {
        List<ClassInfo> results = classSearchService.searchByPrefix(prefix);
        return ResponseEntity.ok(results);
    }

    /**
     * Get all classes in alphabetical order
     * GET /api/search/all
     */
    @GetMapping("/all")
    public ResponseEntity<List<ClassInfo>> getAllClasses() {
        List<ClassInfo> results = classSearchService.getAllClasses();
        return ResponseEntity.ok(results);
    }

    /**
     * Check if a class exists
     * GET /api/search/exists?name=BookingService
     */
    @GetMapping("/exists")
    public ResponseEntity<Boolean> classExists(@RequestParam String name) {
        boolean exists = classSearchService.classExists(name);
        return ResponseEntity.ok(exists);
    }

    /**
     * Get tree statistics
     * GET /api/search/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getTreeStats() {
        return ResponseEntity.ok(new TreeStats(
            classSearchService.getTotalClasses(),
            classSearchService.getTreeHeight(),
            classSearchService.isEmpty()
        ));
    }

    /**
     * Inner class for tree statistics response
     */
    public static class TreeStats {
        public int totalClasses;
        public int treeHeight;
        public boolean isEmpty;

        public TreeStats(int totalClasses, int treeHeight, boolean isEmpty) {
            this.totalClasses = totalClasses;
            this.treeHeight = treeHeight;
            this.isEmpty = isEmpty;
        }
    }
}
