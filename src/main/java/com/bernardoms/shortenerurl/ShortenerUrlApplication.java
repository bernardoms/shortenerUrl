package com.bernardoms.shortenerurl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableCaching
@SpringBootApplication
@EnableMongoRepositories(value = "com.bernardoms.shortenerurl.repository")
public class ShortenerUrlApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShortenerUrlApplication.class, args);
    }

}
