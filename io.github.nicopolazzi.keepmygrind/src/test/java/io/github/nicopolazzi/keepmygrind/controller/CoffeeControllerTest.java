package io.github.nicopolazzi.keepmygrind.controller;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;
import io.github.nicopolazzi.keepmygrind.view.CoffeeView;
import io.github.nicopolazzi.keepmygrind.view.GrindProfileView;

@ExtendWith(MockitoExtension.class)
class CoffeeControllerTest {

    private static final String COFFEE_FIXTURE_ID = "1";
    private static final String COFFEE_FIXTURE_ORIGIN = "origin";
    private static final String COFFEE_FIXTURE_PROCESS = "process";

    @Mock
    private CoffeeRepository coffeeRepository;

    @Mock
    private CoffeeView coffeeView;

    @Mock
    private GrindProfileView grindProfileView;

    @InjectMocks
    private CoffeeController coffeeController;

    @Test
    void testAllCoffees() {
        List<Coffee> coffees = asList(new Coffee());
        when(coffeeRepository.findAll()).thenReturn(coffees);
        coffeeController.allCoffees();
        verify(coffeeView).showAllCoffees(coffees);
    }

    @Test
    void testNewCoffeeWhenCoffeeDoesntAlreadyExist() {
        var coffee = new Coffee(COFFEE_FIXTURE_ID, COFFEE_FIXTURE_ORIGIN, COFFEE_FIXTURE_PROCESS);
        when(coffeeRepository.findById(COFFEE_FIXTURE_ID)).thenReturn(Optional.empty());
        when(coffeeRepository.findAll()).thenReturn(asList(coffee));
        coffeeController.newCoffee(coffee);
        InOrder inOrder = inOrder(coffeeRepository, coffeeView, grindProfileView);
        inOrder.verify(coffeeRepository).save(coffee);
        inOrder.verify(coffeeView).coffeeAdded(coffee);
        inOrder.verify(grindProfileView).refreshCoffees(asList(coffee));
    }

    @Test
    void testNewCoffeeWhenCoffeeAlreadyExists() {
        var coffeeToAdd = new Coffee(COFFEE_FIXTURE_ID, COFFEE_FIXTURE_ORIGIN, COFFEE_FIXTURE_PROCESS);
        var existingCoffee = new Coffee(COFFEE_FIXTURE_ID, "origin2", "process2");
        when(coffeeRepository.findById(COFFEE_FIXTURE_ID)).thenReturn(Optional.of(existingCoffee));
        coffeeController.newCoffee(coffeeToAdd);
        verify(coffeeView).showExistingCoffeeError(existingCoffee);
        verifyNoMoreInteractions(ignoreStubs(coffeeRepository));
    }

    @Test
    void testDeleteCoffeeWhenCoffeeAlreadyExists() {
        var coffee = new Coffee(COFFEE_FIXTURE_ID, COFFEE_FIXTURE_ORIGIN, COFFEE_FIXTURE_PROCESS);
        when(coffeeRepository.findById(COFFEE_FIXTURE_ID)).thenReturn(Optional.of(coffee));
        coffeeController.deleteCoffee(coffee);
        InOrder inOrder = inOrder(coffeeRepository, coffeeView);
        inOrder.verify(coffeeRepository).delete(COFFEE_FIXTURE_ID);
        inOrder.verify(coffeeView).coffeeRemoved(coffee);
    }

    @Test
    void testDeleteCoffeeWhenCoffeeDoesntAlreadyExist() {
        var coffee = new Coffee(COFFEE_FIXTURE_ID, COFFEE_FIXTURE_ORIGIN, COFFEE_FIXTURE_PROCESS);
        when(coffeeRepository.findById(COFFEE_FIXTURE_ID)).thenReturn(Optional.empty());
        coffeeController.deleteCoffee(coffee);
        verify(coffeeView).showNotExistingCoffeeError(coffee);
        verifyNoMoreInteractions(ignoreStubs(coffeeRepository));
    }
}
