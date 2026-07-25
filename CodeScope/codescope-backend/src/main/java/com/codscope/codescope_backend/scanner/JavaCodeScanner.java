package com.codscope.codescope_backend.scanner;

import com.codscope.codescope_backend.graph.Graph;
import com.codscope.codescope_backend.model.ClassInfo;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

public class JavaCodeScanner {

    private final List<ClassInfo> scannedClasses = new ArrayList<>();

    public List<ClassInfo> scanProject(String projectPath) {

        scannedClasses.clear();

        File root = new File(projectPath);

        if (!root.exists()) {
            System.out.println("Project path does not exist: " + projectPath);
            return scannedClasses;
        }

        if (!root.isDirectory()) {
            System.out.println("The selected path is not a folder.");
            return scannedClasses;
        }

        scanDirectory(root);

        return scannedClasses;
    }

    public void buildDependencies(List<ClassInfo> classes,
                                  Graph graph) {

        graph.createNodes(classes);
        detectDependencies(classes, graph);
    }

    private void scanDirectory(File directory) {

        File[] files = directory.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {

            if (file.isDirectory()) {

                scanDirectory(file);

            } else if (file.getName().endsWith(".java")) {

                scanJavaFile(file);

            }
        }
    }

    private void scanJavaFile(File file) {

        try {

            CompilationUnit cu = StaticJavaParser.parse(file);

            String packageName = cu.getPackageDeclaration()
                    .map(pkg -> pkg.getNameAsString())
                    .orElse("");

            for (TypeDeclaration<?> type : cu.getTypes()) {

                ClassInfo classInfo = new ClassInfo(
                        type.getNameAsString(),
                        packageName,
                        file.getAbsolutePath()
                );

                scannedClasses.add(classInfo);

                System.out.println(
                        "Class found : " +
                                packageName + "." +
                                type.getNameAsString()
                );
            }

        } catch (IOException e) {

            System.out.println("Cannot read file : " + file.getAbsolutePath());

        } catch (Exception e) {

            System.out.println("Cannot parse file : " + file.getAbsolutePath());

        }
    }

    private void detectDependencies(List<ClassInfo> scannedClasses,
                                    Graph graph) {

        for (ClassInfo sourceClass : scannedClasses) {

            try {
                String sourceCode = Files.readString(
                        Path.of(sourceClass.getFilePath())
                );

                // Remove comments to reduce incorrect matches
                sourceCode = removeComments(sourceCode);

                for (ClassInfo possibleDependency : scannedClasses) {

                    // A class should not depend on itself
                    if (sourceClass.getFullClassName()
                            .equals(possibleDependency.getFullClassName())) {

                        continue;
                    }

                    if (usesClass(sourceCode, possibleDependency)) {

                        graph.addDependency(
                                sourceClass.getFullClassName(),
                                possibleDependency.getFullClassName()
                        );

                        System.out.println(
                                "Dependency found: " +
                                        sourceClass.getClassName() +
                                        " → " +
                                        possibleDependency.getClassName()
                        );
                    }
                }

            } catch (IOException exception) {

                System.out.println(
                        "Could not read file: " +
                                sourceClass.getFilePath()
                );
            }
        }
    }

    private boolean usesClass(String sourceCode,
                              ClassInfo possibleDependency) {

        String simpleClassName =
                possibleDependency.getClassName();

        String fullClassName =
                possibleDependency.getFullClassName();

        String simpleNamePattern =
                "\\b" + Pattern.quote(simpleClassName) + "\\b";

        String fullNamePattern =
                "\\b" + Pattern.quote(fullClassName) + "\\b";

        return Pattern.compile(simpleNamePattern)
                .matcher(sourceCode)
                .find()
                ||
                Pattern.compile(fullNamePattern)
                        .matcher(sourceCode)
                        .find();
    }

    private String removeComments(String sourceCode) {

        // Remove block comments
        sourceCode = sourceCode.replaceAll(
                "(?s)/\\*.*?\\*/",
                ""
        );

        // Remove single-line comments
        sourceCode = sourceCode.replaceAll(
                "//.*",
                ""
        );

        return sourceCode;
    }
}