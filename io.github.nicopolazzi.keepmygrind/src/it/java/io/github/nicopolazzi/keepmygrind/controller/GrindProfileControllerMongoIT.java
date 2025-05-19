package io.github.nicopolazzi.keepmygrind.controller;

import static java.util.Arrays.asList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.mongodb.MongoClient;

import static org.mockito.Mockito.verify;

import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.model.GrindProfile;
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;
import io.github.nicopolazzi.keepmygrind.repository.GrindProfileRepository;
import io.github.nicopolazzi.keepmygrind.repository.mongo.CoffeeMongoRepository;
import io.github.nicopolazzi.keepmygrind.repository.mongo.GrindProfileMongoRepository;
import io.github.nicopolazzi.keepmygrind.view.GrindProfileView;

@ExtendWith(MockitoExtension.class)
@Testcontainers
class GrindProfileControllerMongoIT {
    private static final String GRINDPROFILE_FIXTURE_ID = "1";
    private static final Coffee GRINDPROFILE_FIXTURE_COFFEE = new Coffee("1", "test", "test");
    private static final String GRINDPROFILE_FIXTURE_BREW = "V60";
    private static final int GRINDPROFILE_FIXTURE_BEANS_GRAMS = 18;
    private static final int GRINDPROFILE_FIXTURE_WATER_MILLILITERS = 250;
    private static final int GRINDPROFILE_FIXTURE_CLICKS = 60;

    @Mock
    private GrindProfileView grindProfileView;
    private GrindProfileRepository grindProfileRepository;
    private GrindProfileController grindProfileController;
    private CoffeeRepository coffeeRepository;

    @Container
    static final MongoDBContainer mongo = new MongoDBContainer("mongo:5");

    @BeforeEach
    void setup() {
        grindProfileRepository = new GrindProfileMongoRepository(
                new MongoClient(mongo.getHost(), mongo.getFirstMappedPort()));
        coffeeRepository = new CoffeeMongoRepository(new MongoClient(mongo.getHost(), mongo.getFirstMappedPort()));
        grindProfileRepository.findAll().forEach(grindProfile -> grindProfileRepository.delete(grindProfile.getId()));
        coffeeRepository.delete(GRINDPROFILE_FIXTURE_ID);
        grindProfileController = new GrindProfileController(grindProfileRepository, grindProfileView, coffeeRepository);
    }

    @Test
    void testAllGrindProfiles() {
        var profile = new GrindProfile(GRINDPROFILE_FIXTURE_ID, GRINDPROFILE_FIXTURE_COFFEE, GRINDPROFILE_FIXTURE_BREW,
                GRINDPROFILE_FIXTURE_BEANS_GRAMS, GRINDPROFILE_FIXTURE_WATER_MILLILITERS, GRINDPROFILE_FIXTURE_CLICKS);
        coffeeRepository.save(GRINDPROFILE_FIXTURE_COFFEE);
        grindProfileRepository.save(profile);
        grindProfileController.allGrindProfiles();
        verify(grindProfileView).showAllGrindProfiles(asList(profile));
    }

    @Test
    void testNewGrindProfile() {
        var profile = new GrindProfile(GRINDPROFILE_FIXTURE_ID, GRINDPROFILE_FIXTURE_COFFEE, GRINDPROFILE_FIXTURE_BREW,
                GRINDPROFILE_FIXTURE_BEANS_GRAMS, GRINDPROFILE_FIXTURE_WATER_MILLILITERS, GRINDPROFILE_FIXTURE_CLICKS);
        coffeeRepository.save(GRINDPROFILE_FIXTURE_COFFEE);
        grindProfileController.newGrindProfile(profile);
        verify(grindProfileView).grindProfileAdded(profile);
    }

    @Test
    void testDeleteGrindProfile() {
        var profile = new GrindProfile(GRINDPROFILE_FIXTURE_ID, GRINDPROFILE_FIXTURE_COFFEE, GRINDPROFILE_FIXTURE_BREW,
                GRINDPROFILE_FIXTURE_BEANS_GRAMS, GRINDPROFILE_FIXTURE_WATER_MILLILITERS, GRINDPROFILE_FIXTURE_CLICKS);
        coffeeRepository.save(GRINDPROFILE_FIXTURE_COFFEE);
        grindProfileRepository.save(profile);
        grindProfileController.deleteGrindProfile(profile);
        verify(grindProfileView).grindProfileRemoved(profile);
    }

}
