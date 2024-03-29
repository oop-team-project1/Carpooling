package com.company.carpooling;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@SecurityScheme(name = "basic", type = SecuritySchemeType.HTTP, scheme = "basic")
public class CarpoolingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarpoolingApplication.class, args);
    }

}
