package com.example.feignexample.controller;

import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/feign-example")
@Slf4j
public class MainController {

    @GetMapping(path = "/hello/{name}")
    @Observed()
    String getHello(@PathVariable String name){
        log.info("We are at the feign-example-service");

        return "Hello " + name;
    }
}
