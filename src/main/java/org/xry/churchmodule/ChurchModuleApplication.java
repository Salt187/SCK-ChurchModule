package org.xry.churchmodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.xry.churchmodule", "org.xry.interceptors"})
public class ChurchModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChurchModuleApplication.class, args);
    }

}
