package com.example.elkexample.controller;

import com.example.elkexample.feign.FeignExampleRest;
import io.micrometer.observation.annotation.Observed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
@EnableFeignClients
@AllArgsConstructor
@Slf4j
public class MainController {


    FeignExampleRest feignExampleRest;
    private RestTemplate restTemplate;


    @GetMapping("/{name}")
    @Observed
    public ResponseEntity<String> get(@PathVariable String name) {
        log.info("We are going to call feign-example-service");
        String hello = feignExampleRest.getHello(name);
        return ResponseEntity.ok(hello);

    }


    @GetMapping("/second/{name}")
    @Observed()
    public ResponseEntity<String> getHello(@PathVariable String name) {
        log.info("We are going to call feign-example-service second time");

        String url = String.format("http://localhost:8081/feign-example/hello/%s", name);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);


        return response;

    }
}
