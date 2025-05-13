package io.github.nicopolazzi.keepmygrind.repository.mongo;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;

@Testcontainers
class CoffeeMongoRepositoryTestcontainersIT {

    @Container
    static final MongoDBContainer mongo = new MongoDBContainer("mongo:5");

    private MongoClient client;
    private MongoCollection<Coffee> coffeeCollection;
    private CoffeeRepository coffeeRepository;

    @BeforeEach
    void setup() {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        client = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getMappedPort(27017)));
        coffeeRepository = new CoffeeMongoRepository(client);
        MongoDatabase database = client.getDatabase(CoffeeMongoRepository.KEEPMYGRIND_DB_NAME)
                .withCodecRegistry(pojoCodecRegistry);
        database.drop();
        coffeeCollection = database.getCollection(CoffeeMongoRepository.COFFEE_COLLECTION_NAME, Coffee.class);
    }

    @AfterEach
    void tearDown() {
        client.close();
    }

    @Test
    void test() {
    }

}
