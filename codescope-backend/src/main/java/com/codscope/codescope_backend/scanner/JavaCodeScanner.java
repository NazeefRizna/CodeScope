package com.codscope.codescope_backend.scanner;

import com.codscope.codescope_backend.model.ClassInfo;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
}