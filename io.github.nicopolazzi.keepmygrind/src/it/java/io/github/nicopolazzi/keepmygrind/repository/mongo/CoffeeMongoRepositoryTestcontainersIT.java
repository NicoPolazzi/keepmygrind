package io.github.nicopolazzi.keepmygrind.repository.mongo;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.Optional;

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

    private static final String COFFEE_FIXTURE_1_ID = "1";
    private static final String COFFEE_FIXTURE_1_ORIGIN = "origin1";
    private static final String COFFEE_FIXTURE_1_PROCESS = "process1";
    private static final String COFFEE_FIXTURE_2_ID = "2";
    private static final String COFFEE_FIXTURE_2_ORIGIN = "origin2";
    private static final String COFFEE_FIXTURE_2_PROCESS = "process2";

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
    void testFindAll() {
        var coffee1 = new Coffee(COFFEE_FIXTURE_1_ID, COFFEE_FIXTURE_1_ORIGIN, COFFEE_FIXTURE_1_PROCESS);
        var coffee2 = new Coffee(COFFEE_FIXTURE_2_ID, COFFEE_FIXTURE_2_ORIGIN, COFFEE_FIXTURE_2_PROCESS);
        coffeeCollection.insertMany(asList(coffee1, coffee2));
        assertThat(coffeeRepository.findAll()).containsExactly(coffee1, coffee2);
    }

    @Test
    void testFindById() {
        var coffee1 = new Coffee(COFFEE_FIXTURE_1_ID, COFFEE_FIXTURE_1_ORIGIN, COFFEE_FIXTURE_1_PROCESS);
        var coffee2 = new Coffee(COFFEE_FIXTURE_2_ID, COFFEE_FIXTURE_2_ORIGIN, COFFEE_FIXTURE_2_PROCESS);
        coffeeCollection.insertMany(asList(coffee1, coffee2));
        assertThat(coffeeRepository.findById(COFFEE_FIXTURE_2_ID)).isEqualTo(Optional.of(coffee2));
    }

    @Test
    void testSave() {
        var coffee = new Coffee(COFFEE_FIXTURE_1_ID, COFFEE_FIXTURE_1_ORIGIN, COFFEE_FIXTURE_1_PROCESS);
        coffeeRepository.save(coffee);
        assertThat(coffeeCollection.find()).containsExactly(coffee);
    }

    @Test
    void testDelete() {
        var coffee = new Coffee(COFFEE_FIXTURE_1_ID, COFFEE_FIXTURE_1_ORIGIN, COFFEE_FIXTURE_1_PROCESS);
        coffeeCollection.insertOne(coffee);
        coffeeRepository.delete(COFFEE_FIXTURE_1_ID);
        assertThat(coffeeCollection.find()).isEmpty();
    }

}
