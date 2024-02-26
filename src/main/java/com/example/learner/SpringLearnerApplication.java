package com.example.learner;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.TimeZone;

@Log4j2
@SpringBootApplication
public class SpringLearnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringLearnerApplication.class, args);
    }
}
