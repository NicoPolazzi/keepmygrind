package io.github.nicopolazzi.keepmygrind.repository.mongo;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Optional;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;

class CoffeeMongoRepositoryTest {

    private static MongoServer server;
    private static InetSocketAddress serverAddress;

    private MongoClient client;
    private MongoCollection<Coffee> coffeeCollection;
    private CoffeeRepository coffeeRepository;

    @BeforeAll
    static void setUpServer() {
        server = new MongoServer(new MemoryBackend());
        serverAddress = server.bind();
    }

    @AfterAll
    static void shutdownServer() {
        server.shutdown();
    }

    @BeforeEach
    void setup() {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        client = new MongoClient(new ServerAddress(serverAddress));
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
    void testFindAllWhenDatabaseIsEmpty() {
        assertThat(coffeeRepository.findAll()).isEmpty();
    }

    @Test
    void testFindAllWhenDatabaseIsNotEmpty() {
        var coffee1 = new Coffee("1", "testOrigin", "testProcessMethod", "testRoastMethod");
        var coffee2 = new Coffee("2", "testOrigin", "testProcessMethod", "testRoastMethod");
        List<Coffee> coffees = asList(coffee1, coffee2);
        coffeeCollection.insertMany(coffees);
        assertThat(coffeeRepository.findAll()).containsExactly(coffee1, coffee2);
    }

    @Test
    void testFindByIdNotFound() {
        assertThat(coffeeRepository.findById("1")).isEmpty();
    }

    @Test
    void testFindByIdFound() {
        var coffee1 = new Coffee("1", "testOrigin", "testProcessMethod", "testRoastMethod");
        var coffee2 = new Coffee("2", "testOrigin", "testProcessMethod", "testRoastMethod");
        List<Coffee> coffees = asList(coffee1, coffee2);
        coffeeCollection.insertMany(coffees);
        assertThat(coffeeRepository.findById("2")).isEqualTo(Optional.of(coffee2));
    }

}
