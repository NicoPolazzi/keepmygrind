package io.github.nicopolazzi.keepmygrind.repository.sql;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.model.GrindProfile;
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;

class CoffeeSqlRepositoryTest {

    private static final String COFFEE_FIXTURE_1_ID = "1";
    private static final String COFFEE_FIXTURE_1_ORIGIN = "origin1";
    private static final String COFFEE_FIXTURE_1_PROCESS = "process1";
    private static final String COFFEE_FIXTURE_2_ID = "2";
    private static final String COFFEE_FIXTURE_2_ORIGIN = "origin2";
    private static final String COFFEE_FIXTURE_2_PROCESS = "process2";

    private static SessionFactory sessionFactory;
    private CoffeeRepository coffeeRepository;

    @BeforeAll
    static void setupSessionFactory() {
        sessionFactory = new Configuration().addAnnotatedClass(Coffee.class).addAnnotatedClass(GrindProfile.class)
                .setProperty(AvailableSettings.JAKARTA_JDBC_URL, "jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1")
                .setProperty(AvailableSettings.JAKARTA_HBM2DDL_DATABASE_ACTION, Action.ACTION_CREATE_THEN_DROP)
                .buildSessionFactory();
    }

    @AfterAll
    static void tearDown() {
        sessionFactory.close();
    }

    @BeforeEach
    void clearDatabase() {
        sessionFactory.getSchemaManager().truncateMappedObjects();
        coffeeRepository = new CoffeeSqlRepository(sessionFactory);
    }

    @Test
    void testFindAllWhenDatabaseIsEmpty() {
        assertThat(coffeeRepository.findAll()).isEmpty();
    }

    @Test
    void testFindAllWhenDatabaseIsNotEmpty() {
        var coffee1 = new Coffee(COFFEE_FIXTURE_1_ID, COFFEE_FIXTURE_1_ORIGIN, COFFEE_FIXTURE_1_PROCESS);
        var coffee2 = new Coffee(COFFEE_FIXTURE_2_ID, COFFEE_FIXTURE_2_ORIGIN, COFFEE_FIXTURE_2_PROCESS);
        sessionFactory.inTransaction(session -> {
            session.persist(coffee1);
            session.persist(coffee2);
        });
        assertThat(coffeeRepository.findAll()).containsExactly(coffee1, coffee2);
    }

    @Test
    void testFindByIdNotFound() {
        assertThat(coffeeRepository.findById(COFFEE_FIXTURE_1_ID)).isEmpty();
    }

    @Test
    void testFindByIdFound() {
        var coffee1 = new Coffee(COFFEE_FIXTURE_1_ID, COFFEE_FIXTURE_1_ORIGIN, COFFEE_FIXTURE_1_PROCESS);
        var coffee2 = new Coffee(COFFEE_FIXTURE_2_ID, COFFEE_FIXTURE_2_ORIGIN, COFFEE_FIXTURE_2_PROCESS);
        sessionFactory.inTransaction(session -> {
            session.persist(coffee1);
            session.persist(coffee2);
        });
        assertThat(coffeeRepository.findById(COFFEE_FIXTURE_2_ID)).isEqualTo(Optional.of(coffee2));
    }

    @Test
    void testSave() {
        var coffee = new Coffee(COFFEE_FIXTURE_1_ID, COFFEE_FIXTURE_1_ORIGIN, COFFEE_FIXTURE_1_PROCESS);
        coffeeRepository.save(coffee);
        List<Coffee> coffees = sessionFactory
                .fromSession(session -> session.createSelectionQuery("from Coffee", Coffee.class).getResultList());
        assertThat(coffees).containsExactly(coffee);
    }

    @Test
    void testSaveShouldAlsoSaveRelatedGrindProfiles() {
        var coffee = new Coffee(COFFEE_FIXTURE_1_ID, COFFEE_FIXTURE_1_ORIGIN, COFFEE_FIXTURE_1_PROCESS);
        var profile = new GrindProfile("1", coffee, "espresso", 10, 100, 30);
        coffee.addGrindProfile(profile);
        coffeeRepository.save(coffee);
        Coffee retrivedCoffee = sessionFactory.fromSession(session -> session.find(Coffee.class, COFFEE_FIXTURE_1_ID));
        GrindProfile retrivedGrindProfile = sessionFactory
                .fromSession(session -> session.find(GrindProfile.class, "1"));
        assertThat(retrivedCoffee).isEqualTo(coffee);
        assertThat(retrivedGrindProfile).isEqualTo(profile);
    }

    @Test
    void testDelete() {
        var coffee = new Coffee(COFFEE_FIXTURE_1_ID, COFFEE_FIXTURE_1_ORIGIN, COFFEE_FIXTURE_1_PROCESS);
        sessionFactory.inTransaction(session -> session.persist(coffee));
        coffeeRepository.delete(COFFEE_FIXTURE_1_ID);
        Coffee retrivedCoffee = sessionFactory.fromSession(session -> session.find(Coffee.class, COFFEE_FIXTURE_1_ID));
        assertThat(retrivedCoffee).isNull();
    }

    @Test
    void testDeleteShouldAlsoDeleteRelatedGrindProfiles() {
        var coffee = new Coffee(COFFEE_FIXTURE_1_ID, COFFEE_FIXTURE_1_ORIGIN, COFFEE_FIXTURE_1_PROCESS);
        var profile1 = new GrindProfile("1", coffee, "espresso", 10, 100, 30);
        var profile2 = new GrindProfile("2", coffee, "espresso", 10, 100, 30);
        coffee.addGrindProfile(profile1);
        coffee.addGrindProfile(profile2);
        sessionFactory.inTransaction(session -> session.persist(coffee));
        coffeeRepository.delete(COFFEE_FIXTURE_1_ID);
        List<GrindProfile> profiles = sessionFactory.fromSession(
                session -> session.createSelectionQuery("from GrindProfile", GrindProfile.class).getResultList());
        assertThat(profiles).isEmpty();
    }

}
