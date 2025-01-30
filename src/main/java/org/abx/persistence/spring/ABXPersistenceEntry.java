package org.abx.persistence.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "org.abx.persistence.spring",
        "org.abx.persistence.utils",
        "org.abx.persistence.repository",
        "org.abx.persistence.creds",
        "org.abx.persistence.controller"})
public class ABXPersistenceEntry {

    public static void main(String[] args) {
        SpringApplication.run(ABXPersistenceEntry.class, args);

    }

}
