package com.codscope.codescope_backend;

import com.codscope.codescope_backend.heap.HeapNode;
import com.codscope.codescope_backend.model.ClassInfo;
import com.codscope.codescope_backend.service.CodeScopeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CodescopeBackendApplication
        implements CommandLineRunner {
    

    public static void main(String[] args) {

        SpringApplication.run(
                CodescopeBackendApplication.class,
                args
        );
    }

    @Override
    public void run(String... args) {

    }
}