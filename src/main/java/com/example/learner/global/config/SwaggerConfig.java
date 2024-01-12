/*
 * author : duckbill413
 * created date : 2024-01-07
 * updated date : 2024-01-07
 * description :
 */

package com.example.learner.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    //    private final ServerProperties serverProperties;
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("Learn Spring")
                .description("""
                        <h2>Spring Learner Swagger doc API</h1>
                        <p style="text-align: right">by duckbill413</p>
                        <h3 style="text-align: center">Spring</h2>
                        <ul>
                          <li>Spring package structure</li>
                          <li>JDK 17</li>
                          <li>ResponseEntity</li>
                          <li>ControllerAdvice</li>
                          <li>SwaggerDoc</li>
                        </ul>
                        """)
                .contact(new Contact().name("duckbill413").url("https://github.com/duckbill413").email("uhyeon7399@daum.net"))
                .license(new License().name("Apache License Version 2.0").url("http://www.apache.org/licenses/LICENSE-2.0"))
                .version("v0.0.1")
        );
    }
}
