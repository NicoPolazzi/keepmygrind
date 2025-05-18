package io.github.nicopolazzi.keepmygrind.repository.mongo;

import static io.github.nicopolazzi.keepmygrind.repository.mongo.GrindProfileMongoRepository.GRINDPROFILE_COLLECTION_NAME;
import static io.github.nicopolazzi.keepmygrind.repository.mongo.GrindProfileMongoRepository.KEEPMYGRIND_DB_NAME;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.net.InetSocketAddress;
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
import io.github.nicopolazzi.keepmygrind.model.GrindProfile;
import io.github.nicopolazzi.keepmygrind.repository.GrindProfileRepository;

class GrindProfileMongoRepositoryTest {
    private static final Coffee GRINDPROFILE_FIXTURE_COFFEE = new Coffee("1", "test", "test");

    private static final String GRINDPROFILE_FIXTURE_1_ID = "1";
    private static final String GRINDPROFILE_FIXTURE_1_BREW = "V60";
    private static final int GRINDPROFILE_FIXTURE_1_BEANS_GRAMS = 18;
    private static final int GRINDPROFILE_FIXTURE_1_WATER_MILLILITERS = 250;
    private static final int GRINDPROFILE_FIXTURE_1_CLICKS = 60;
    private static final String GRINDPROFILE_FIXTURE_2_ID = "2";
    private static final String GRINDPROFILE_FIXTURE_2_BREW = "espresso";
    private static final int GRINDPROFILE_FIXTURE_2_BEANS_GRAMS = 9;
    private static final int GRINDPROFILE_FIXTURE_2_WATER_MILLILITERS = 50;
    private static final int GRINDPROFILE_FIXTURE_2_CLICKS = 20;

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

    @Test
    void testFindAllWhenDatabaseIsNotEmpty() {
        var profile1 = new GrindProfile(GRINDPROFILE_FIXTURE_1_ID, GRINDPROFILE_FIXTURE_COFFEE,
                GRINDPROFILE_FIXTURE_1_BREW, GRINDPROFILE_FIXTURE_1_BEANS_GRAMS,
                GRINDPROFILE_FIXTURE_1_WATER_MILLILITERS, GRINDPROFILE_FIXTURE_1_CLICKS);
        var profile2 = new GrindProfile(GRINDPROFILE_FIXTURE_2_ID, GRINDPROFILE_FIXTURE_COFFEE,
                GRINDPROFILE_FIXTURE_2_BREW, GRINDPROFILE_FIXTURE_2_BEANS_GRAMS,
                GRINDPROFILE_FIXTURE_2_WATER_MILLILITERS, GRINDPROFILE_FIXTURE_2_CLICKS);

        grindProfileCollection.insertMany(asList(profile1, profile2));
        assertThat(grindProfileRepository.findAll()).containsExactly(profile1, profile2);
    }

    @Test
    void testFindByIdNotFound() {
        assertThat(grindProfileRepository.findById(GRINDPROFILE_FIXTURE_1_ID)).isEmpty();
    }

    @Test
    void testFindByIdFound() {
        var profile1 = new GrindProfile(GRINDPROFILE_FIXTURE_1_ID, GRINDPROFILE_FIXTURE_COFFEE,
                GRINDPROFILE_FIXTURE_1_BREW, GRINDPROFILE_FIXTURE_1_BEANS_GRAMS,
                GRINDPROFILE_FIXTURE_1_WATER_MILLILITERS, GRINDPROFILE_FIXTURE_1_CLICKS);
        var profile2 = new GrindProfile(GRINDPROFILE_FIXTURE_2_ID, GRINDPROFILE_FIXTURE_COFFEE,
                GRINDPROFILE_FIXTURE_2_BREW, GRINDPROFILE_FIXTURE_2_BEANS_GRAMS,
                GRINDPROFILE_FIXTURE_2_WATER_MILLILITERS, GRINDPROFILE_FIXTURE_2_CLICKS);

        grindProfileCollection.insertMany(asList(profile1, profile2));
        assertThat(grindProfileRepository.findById(GRINDPROFILE_FIXTURE_2_ID)).isEqualTo(Optional.of(profile2));
    }

    @Test
    void testSave() {
        var profile = new GrindProfile(GRINDPROFILE_FIXTURE_1_ID, GRINDPROFILE_FIXTURE_COFFEE,
                GRINDPROFILE_FIXTURE_1_BREW, GRINDPROFILE_FIXTURE_1_BEANS_GRAMS,
                GRINDPROFILE_FIXTURE_1_WATER_MILLILITERS, GRINDPROFILE_FIXTURE_1_CLICKS);
        grindProfileRepository.save(profile);
        assertThat(grindProfileCollection.find()).containsExactly(profile);
    }
}
