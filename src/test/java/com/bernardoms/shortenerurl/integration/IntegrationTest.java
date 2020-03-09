package com.bernardoms.shortenerurl.integration;

import com.bernardoms.shortenerurl.model.URLShortener;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
public abstract class IntegrationTest {


    private static boolean alreadySaved = false;

    @Autowired
    MongoTemplate mongoTemplate;


    @BeforeEach
    public void setUp() {

        if (alreadySaved) {
            return;
        }
        mongoTemplate.save(URLShortener.builder().originalURL("http://www.test.com").alias("abdefg").build());
        alreadySaved = true;
    }
}
