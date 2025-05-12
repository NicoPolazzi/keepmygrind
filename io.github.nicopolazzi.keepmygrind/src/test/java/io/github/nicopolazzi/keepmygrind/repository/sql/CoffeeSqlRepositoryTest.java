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
        sessionFactory = new Configuration().addAnnotatedClass(Coffee.class)
                .setProperty(AvailableSettings.JAKARTA_JDBC_URL, "jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1")
                .setProperty(AvailableSettings.JAKARTA_HBM2DDL_DATABASE_ACTION, Action.ACTION_CREATE_THEN_DROP)
                .buildSessionFactory();
    }

    @AfterAll
    static void tearDown() {
        sessionFactory.close();
    }

    @BeforeEach
    void setup() {
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

}
