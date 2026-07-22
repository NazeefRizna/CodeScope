package com.codscope.codescope_backend;

import com.codscope.codescope_backend.model.ClassInfo;
import com.codscope.codescope_backend.scanner.JavaCodeScanner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class CodescopeBackendApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CodescopeBackendApplication.class, args);
    }

    @Override
    public void run(String... args) {

        String projectPath = "C:/Users/nazee/Desktop/Co";

        JavaCodeScanner scanner = new JavaCodeScanner();

        System.out.println("\nScanning project: " + projectPath);

        List<ClassInfo> classes = scanner.scanProject(projectPath);

        System.out.println("\n ---CLASSES FOUND---\n");

        if (classes.isEmpty()) {
            System.out.println("No Java classes were found.");
            return;
        }

        for (ClassInfo classInfo : classes) {

            System.out.println("Class      : " + classInfo.getClassName());
            System.out.println("Package    : " + classInfo.getPackageName());
            System.out.println("File Path  : " + classInfo.getFilePath());

            System.out.println("-----------------------------------");
        }

        System.out.println("\nTotal Classes Found : " + classes.size());
    }
}