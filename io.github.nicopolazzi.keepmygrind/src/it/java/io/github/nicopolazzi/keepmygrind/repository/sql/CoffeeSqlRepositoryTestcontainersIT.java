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
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;

@Testcontainers
class CoffeeSqlRepositoryTestcontainersIT {

    private static final String COFFEE_FIXTURE_1_ID = "1";
    private static final String COFFEE_FIXTURE_1_ORIGIN = "origin1";
    private static final String COFFEE_FIXTURE_1_PROCESS = "process1";
    private static final String COFFEE_FIXTURE_2_ID = "2";
    private static final String COFFEE_FIXTURE_2_ORIGIN = "origin2";
    private static final String COFFEE_FIXTURE_2_PROCESS = "process2";

    @Container
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8");

    private static SessionFactory sessionFactory;
    private CoffeeRepository coffeeRepository;

    @BeforeAll
    static void buildSessionFactory() {
        sessionFactory = new Configuration().addAnnotatedClass(Coffee.class)
                .setProperty(AvailableSettings.JAKARTA_JDBC_URL, mysql.getJdbcUrl())
                .setProperty(AvailableSettings.JAKARTA_JDBC_DRIVER, "com.mysql.cj.jdbc.Driver")
                .setProperty(AvailableSettings.JAKARTA_JDBC_USER, mysql.getUsername())
                .setProperty(AvailableSettings.JAKARTA_JDBC_PASSWORD, mysql.getPassword())
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
    void testFindAll() {
        var coffee1 = new Coffee(COFFEE_FIXTURE_1_ID, COFFEE_FIXTURE_1_ORIGIN, COFFEE_FIXTURE_1_PROCESS);
        var coffee2 = new Coffee(COFFEE_FIXTURE_2_ID, COFFEE_FIXTURE_2_ORIGIN, COFFEE_FIXTURE_2_PROCESS);

        sessionFactory.inTransaction(session -> {
            session.persist(coffee1);
            session.persist(coffee2);
        });

        assertThat(coffeeRepository.findAll()).containsExactly(coffee1, coffee2);
    }

    @Test
    void testFindById() {
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
    void testDelete() {
        var coffee = new Coffee(COFFEE_FIXTURE_1_ID, COFFEE_FIXTURE_1_ORIGIN, COFFEE_FIXTURE_1_PROCESS);
        sessionFactory.inTransaction(session -> session.persist(coffee));
        coffeeRepository.delete(COFFEE_FIXTURE_1_ID);
        Coffee retrivedCoffee = sessionFactory.fromSession(session -> session.find(Coffee.class, COFFEE_FIXTURE_1_ID));
        assertThat(retrivedCoffee).isNull();
    }

}
