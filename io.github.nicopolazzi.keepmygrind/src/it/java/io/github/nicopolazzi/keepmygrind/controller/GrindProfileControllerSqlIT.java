package io.github.nicopolazzi.keepmygrind.controller;

import static java.util.Arrays.asList;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.model.GrindProfile;
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;
import io.github.nicopolazzi.keepmygrind.repository.GrindProfileRepository;
import io.github.nicopolazzi.keepmygrind.repository.sql.CoffeeSqlRepository;
import io.github.nicopolazzi.keepmygrind.repository.sql.GrindProfileSqlRepository;
import io.github.nicopolazzi.keepmygrind.view.GrindProfileView;

@ExtendWith(MockitoExtension.class)
class GrindProfileControllerSqlIT {
    private static final String GRINDPROFILE_FIXTURE_ID = "1";
    private static final Coffee GRINDPROFILE_FIXTURE_COFFEE = new Coffee("1", "test", "test");
    private static final String GRINDPROFILE_FIXTURE_BREW = "V60";
    private static final int GRINDPROFILE_FIXTURE_BEANS_GRAMS = 18;
    private static final int GRINDPROFILE_FIXTURE_WATER_MILLILITERS = 250;
    private static final int GRINDPROFILE_FIXTURE_CLICKS = 60;

    private static SessionFactory sessionFactory;

    @Mock
    private GrindProfileView grindProfileView;
    private GrindProfileController grindProfileController;
    private CoffeeRepository coffeeRepository;
    private GrindProfileRepository grindProfileRepository;

    @BeforeAll
    static void setupSessionFactory() {
        sessionFactory = new Configuration().addAnnotatedClass(GrindProfile.class).addAnnotatedClass(Coffee.class)
                .setProperty(AvailableSettings.JAKARTA_JDBC_URL, "jdbc:tc:mysql:8:///keepmygrind")
                .setProperty(AvailableSettings.JAKARTA_HBM2DDL_DATABASE_ACTION, Action.ACTION_CREATE_THEN_DROP)
                .buildSessionFactory();
    }

    @AfterAll
    static void tearDown() {
        sessionFactory.close();
    }

    @BeforeEach
    void setup() {
        coffeeRepository = new CoffeeSqlRepository(sessionFactory);
        grindProfileRepository = new GrindProfileSqlRepository(sessionFactory);
        grindProfileRepository.findAll().forEach(grindProfile -> grindProfileRepository.delete(grindProfile.getId()));
        coffeeRepository.findAll().forEach(coffee -> coffeeRepository.delete(coffee.getId()));
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
