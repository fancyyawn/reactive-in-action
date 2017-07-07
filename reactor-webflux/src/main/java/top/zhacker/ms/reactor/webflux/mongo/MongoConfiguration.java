/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.zhacker.ms.reactor.webflux.mongo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import top.zhacker.ms.reactor.webflux.model.Person;

/**
 * @author Sebastien Deleuze
 * @author Mark Paluch
 */
@Profile("mongo")
@Configuration
@EnableReactiveMongoRepositories
public class MongoConfiguration extends AbstractReactiveMongoConfiguration {

    @Value("${mongo.database}")
    private String database;

    @Bean
    ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json().build();
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create();
    }

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Bean
    CommandLineRunner initData(PersonReactiveCrudRepository personRepository) {
        return (p) -> {
            personRepository.deleteAll().block();
            personRepository.save(new Person("1", "Eric", "Foo")).block();
            personRepository.save(new Person("2", "Raymond", "Bar")).block();
            personRepository.save(new Person("3", "Paul", "Baz")).block();
        };
    }
}
