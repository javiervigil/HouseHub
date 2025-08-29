package com.hh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//@EntityScan({"com.hh.model", "com.hh.data", "com.hh.catalogs", "com.hh.dto"})
public class HouseHubApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(HouseHubApplication.class, args);

        // Obtiene la instancia del bean MonthlyCron del contexto
        MonthlyCron monthlyCron = context.getBean(MonthlyCron.class);
    }

}
