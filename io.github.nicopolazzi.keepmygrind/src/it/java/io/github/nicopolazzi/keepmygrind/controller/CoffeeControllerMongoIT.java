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
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;
import io.github.nicopolazzi.keepmygrind.repository.mongo.CoffeeMongoRepository;
import io.github.nicopolazzi.keepmygrind.view.CoffeeView;

@ExtendWith(MockitoExtension.class)
@Testcontainers
class CoffeeControllerMongoIT {
    private static final String COFFEE_FIXTURE_ID = "1";
    private static final String COFFEE_FIXTURE_ORIGIN = "origin";
    private static final String COFFEE_FIXTURE_PROCESS = "process";

    @Mock
    private CoffeeView coffeeView;
    private CoffeeRepository coffeeRepository;
    private CoffeeController coffeeController;

    @Container
    static final MongoDBContainer mongo = new MongoDBContainer("mongo:5");

    @BeforeEach
    void setup() {
        coffeeRepository = new CoffeeMongoRepository(new MongoClient(mongo.getHost(), mongo.getFirstMappedPort()));
        coffeeRepository.findAll().forEach(coffee -> coffeeRepository.delete(coffee.getId()));
        coffeeController = new CoffeeController(coffeeRepository, coffeeView);
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
