package io.github.nicopolazzi.keepmygrind.repository.mongo;

import static io.github.nicopolazzi.keepmygrind.repository.mongo.GrindProfileMongoRepository.GRINDPROFILE_COLLECTION_NAME;
import static io.github.nicopolazzi.keepmygrind.repository.mongo.GrindProfileMongoRepository.KEEPMYGRIND_DB_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.net.InetSocketAddress;

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
import io.github.nicopolazzi.keepmygrind.model.GrindProfile;
import io.github.nicopolazzi.keepmygrind.repository.GrindProfileRepository;

class GrindProfileMongoRepositoryTest {

    private static MongoServer server;
    private static InetSocketAddress serverAddress;

    private MongoClient client;
    private MongoCollection<GrindProfile> grindProfileCollection;
    private GrindProfileRepository grindProfileRepository;

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
        grindProfileRepository = new GrindProfileMongoRepository(client);
        MongoDatabase database = client.getDatabase(KEEPMYGRIND_DB_NAME).withCodecRegistry(pojoCodecRegistry);
        database.drop();
        grindProfileCollection = database.getCollection(GRINDPROFILE_COLLECTION_NAME, GrindProfile.class);
    }

    @AfterEach
    void tearDown() {
        client.close();
    }

    @Test
    void testFindAllWhenDatabaseIsEmpty() {
        assertThat(grindProfileRepository.findAll()).isEmpty();
    }

}
