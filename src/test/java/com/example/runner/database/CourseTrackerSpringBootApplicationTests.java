package com.example.runner.database;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class CourseTrackerSpringBootApplicationTests {
    @Autowired
    private MongoTemplate mongoTemplate;
    @DisplayName("mongodb 연결 테스트")
    @Test
    public void givenObjectAvailableWhenSaveToCollectionThenExpectValue() {
        // given
        DBObject object = BasicDBObjectBuilder.start()
                .add("duckbill413", "Spring boot With MongoDB")
                .get();

        // when
        mongoTemplate.save(object, "runner");

        // then
        assertThat(mongoTemplate.findAll(DBObject.class, "runner"))
                .extracting("duckbill413")
                .containsOnly("Spring boot With MongoDB");
    }
}
