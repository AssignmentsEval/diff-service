package com.limac.diffservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Starting point of the service.
 */
@SpringBootApplication
@SuppressWarnings("PMD.UseUtilityClass")
public class DiffServiceApplication {

    /**
     * Starting point of springBoot service.
     *
     * @param args string arguments
     */
    public static void main(final String... args) {
        SpringApplication.run(DiffServiceApplication.class, args);
    }
}
