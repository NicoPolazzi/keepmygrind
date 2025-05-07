package io.github.nicopolazzi.keepmygrind.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;

import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;

class CoffeeMongoRepositoryTest {

    private static MongoServer server;
    private static InetSocketAddress serverAddress;

    private MongoClient client;
    private MongoCollection<Document> coffeeCollection;
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
        client = new MongoClient(new ServerAddress(serverAddress));
        coffeeRepository = new CoffeeMongoRepository(client);
        MongoDatabase database = client.getDatabase(CoffeeMongoRepository.KEEPMYGRIND_DB_NAME);
        database.drop();
        coffeeCollection = database.getCollection(CoffeeMongoRepository.COFFEE_COLLECTION_NAME);
    }

    @AfterEach
    void tearDown() {
        client.close();
    }

    @Test
    void testFindAllWhenDatabaseIsEmpty() {
        assertThat(coffeeRepository.findAll()).isEmpty();
    }

}
