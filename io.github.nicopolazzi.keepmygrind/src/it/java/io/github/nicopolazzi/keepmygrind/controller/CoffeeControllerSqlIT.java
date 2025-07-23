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
import io.github.nicopolazzi.keepmygrind.repository.sql.CoffeeSqlRepository;
import io.github.nicopolazzi.keepmygrind.view.CoffeeView;
import io.github.nicopolazzi.keepmygrind.view.GrindProfileView;

@ExtendWith(MockitoExtension.class)
class CoffeeControllerSqlIT {

    private static final String COFFEE_FIXTURE_ID = "1";
    private static final String COFFEE_FIXTURE_ORIGIN = "origin";
    private static final String COFFEE_FIXTURE_PROCESS = "process";

    private static SessionFactory sessionFactory;

    @Mock
    private CoffeeView coffeeView;

    @Mock
    private GrindProfileView grindProfileView;

    private CoffeeRepository coffeeRepository;
    private CoffeeController coffeeController;

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
        coffeeRepository.findAll().forEach(coffee -> coffeeRepository.delete(coffee.getId()));
        coffeeController = new CoffeeController(coffeeRepository, coffeeView, grindProfileView);
    }

    @Test
    void testAllCoffes() {
        var coffee = new Coffee(COFFEE_FIXTURE_ID, COFFEE_FIXTURE_ORIGIN, COFFEE_FIXTURE_PROCESS);
        coffeeRepository.save(coffee);
        coffeeController.allCoffees();
        verify(coffeeView).showAllCoffees(asList(coffee));
    }

    @Test
    void testNewCoffee() {
        var coffee = new Coffee(COFFEE_FIXTURE_ID, COFFEE_FIXTURE_ORIGIN, COFFEE_FIXTURE_PROCESS);
        coffeeController.newCoffee(coffee);
        verify(coffeeView).coffeeAdded(coffee);
    }

    @Test
    void testDeleteCoffee() {
        var coffee = new Coffee(COFFEE_FIXTURE_ID, COFFEE_FIXTURE_ORIGIN, COFFEE_FIXTURE_PROCESS);
        coffeeRepository.save(coffee);
        coffeeController.deleteCoffee(coffee);
        verify(coffeeView).coffeeRemoved(coffee);
    }

}
