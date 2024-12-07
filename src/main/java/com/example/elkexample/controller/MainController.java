package com.example.elkexample.controller;

import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class MainController {

    @GetMapping
    public ResponseEntity<String> get() {
        log.info("Hello");
        return ResponseEntity.ok("Hello World");

    }
}
