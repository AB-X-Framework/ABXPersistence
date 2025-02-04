package org.abx.persistence.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "org.abx.sec",
        "org.abx.persistence.spring",
        "org.abx.services",
        "org.abx.jwt",
        "org.abx.persistence.client",
        "org.abx.persistence.controller"})
public class ABXPersistenceEntry {

    public static void main(String[] args) {
        SpringApplication.run(ABXPersistenceEntry.class, args);

    }

}
