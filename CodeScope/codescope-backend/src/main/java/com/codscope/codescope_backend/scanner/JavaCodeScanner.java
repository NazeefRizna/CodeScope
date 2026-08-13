package com.codscope.codescope_backend.scanner;

import com.codscope.codescope_backend.graph.Graph;
import com.codscope.codescope_backend.model.ClassInfo;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class JavaCodeScanner {

    private final List<ClassInfo> scannedClasses =
            new ArrayList<>();

    public List<ClassInfo> scanProject(
            String projectPath
    ) {

        scannedClasses.clear();

        File root =
                new File(projectPath);

        if (!root.exists()
                || !root.isDirectory()) {

            return scannedClasses;
        }

        scanDirectory(root);

        return new ArrayList<>(
                scannedClasses
        );
    }

    public void buildDependencies(
            List<ClassInfo> classes,
            Graph graph
    ) {

        if (classes == null
                || graph == null) {

            return;
        }

        detectDependencies(
                classes,
                graph
        );
    }

    private void scanDirectory(
            File directory
    ) {

        File[] files =
                directory.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {

            if (file.isDirectory()) {

                scanDirectory(file);

            } else if (file
                    .getName()
                    .endsWith(".java")) {

                scanJavaFile(file);
            }
        }
    }

    private void scanJavaFile(
            File file
    ) {

        try {

            CompilationUnit compilationUnit =
                    StaticJavaParser.parse(file);

            String packageName =
                    compilationUnit
                            .getPackageDeclaration()
                            .map(
                                    declaration ->
                                            declaration
                                                    .getNameAsString()
                            )
                            .orElse("");

            for (TypeDeclaration<?> type :
                    compilationUnit.getTypes()) {

                ClassInfo classInfo =
                        new ClassInfo(
                                type.getNameAsString(),
                                packageName,
                                file.getAbsolutePath()
                        );

                scannedClasses.add(
                        classInfo
                );
            }

        } catch (IOException exception) {

            // File could not be read

        } catch (Exception exception) {

            // File could not be parsed
        }
    }

    private void detectDependencies(
            List<ClassInfo> classes,
            Graph graph
    ) {

        for (ClassInfo sourceClass :
                classes) {

            try {

                String sourceCode =
                        Files.readString(
                                Path.of(
                                        sourceClass
                                                .getFilePath()
                                )
                        );

                sourceCode =
                        removeComments(
                                sourceCode
                        );

                for (ClassInfo possibleDependency :
                        classes) {

                    if (sourceClass
                            .getFullClassName()
                            .equals(
                                    possibleDependency
                                            .getFullClassName()
                            )) {

                        continue;
                    }

                    if (usesClass(
                            sourceCode,
                            possibleDependency
                    )) {

                        graph.addDependency(
                                sourceClass
                                        .getFullClassName(),

                                possibleDependency
                                        .getFullClassName()
                        );
                    }
                }

            } catch (IOException exception) {

                // File could not be read
            }
        }
    }

    private boolean usesClass(
            String sourceCode,
            ClassInfo possibleDependency
    ) {

        String simpleClassName =
                possibleDependency
                        .getClassName();

        String fullClassName =
                possibleDependency
                        .getFullClassName();

        String simpleNamePattern =
                "\\b"
                        + Pattern.quote(
                        simpleClassName
                )
                        + "\\b";

        String fullNamePattern =
                "\\b"
                        + Pattern.quote(
                        fullClassName
                )
                        + "\\b";

        return Pattern
                .compile(simpleNamePattern)
                .matcher(sourceCode)
                .find()

                ||

                Pattern
                        .compile(fullNamePattern)
                        .matcher(sourceCode)
                        .find();
    }

    private String removeComments(
            String sourceCode
    ) {

        sourceCode =
                sourceCode.replaceAll(
                        "(?s)/\\*.*?\\*/",
                        ""
                );

        sourceCode =
                sourceCode.replaceAll(
                        "//.*",
                        ""
                );

        return sourceCode;
    }
}