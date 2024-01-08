package com.example.learner;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SpringLearnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringLearnerApplication.class, args);
    }

    // Favicon Controller
    // https://www.baeldung.com/spring-boot-favicon
    @Controller
    static class FaviconController {
        @GetMapping("/favicon.ico")
        @ResponseBody
        void returnNoFavicon() {
        }
    }

    // Health Checking Controller
    @RestController
    static class HealthController {
        @GetMapping("/health")
        public String healthCheck() {
            return "health OK";
        }
    }
}
