package com.example.elkexample.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "feign-example-service", url = "http://localhost:8081")
public interface FeignExampleRest {

    @GetMapping(path = "/feign-example/hello/{name}")
    String getHello(@PathVariable String name);
}
