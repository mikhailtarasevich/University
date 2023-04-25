package com.mikhail.tarasevich.university;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.mikhail.tarasevich.university"})
@EntityScan(basePackages = {"com.mikhail.tarasevich.university"})
@EnableJpaRepositories(basePackages = {"com.mikhail.tarasevich.university"})
public class UniversityApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniversityApplication.class, args);
    }

}
