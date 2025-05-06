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

@ExtendWith(MockitoExtension.class)
class CoffeeControllerTest {

    @Mock
    private CoffeeRepository coffeeRepository;

    @Mock
    private CoffeeView coffeeView;

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
        var coffee = new Coffee("1", "testOrigin", "testProcessMethod", "testRoastMethod");
        when(coffeeRepository.findById("1")).thenReturn(Optional.empty());
        coffeeController.newCoffee(coffee);
        InOrder inOrder = inOrder(coffeeRepository, coffeeView);
        inOrder.verify(coffeeRepository).save(coffee);
        inOrder.verify(coffeeView).coffeeAdded(coffee);
    }

    @Test
    void testNewCoffeeWhenCoffeeAlreadyExists() {
        var coffeeToAdd = new Coffee("1", "testOrigin", "testProcessMethod", "testRoastMethod");
        var existingCoffee = new Coffee("1", "testOrigin2", "testProcessMethod", "testRoastMethod");
        when(coffeeRepository.findById("1")).thenReturn(Optional.of(existingCoffee));
        coffeeController.newCoffee(coffeeToAdd);
        verify(coffeeView).showExistingCoffeeError(existingCoffee);
        verifyNoMoreInteractions(ignoreStubs(coffeeRepository));

    }

}
