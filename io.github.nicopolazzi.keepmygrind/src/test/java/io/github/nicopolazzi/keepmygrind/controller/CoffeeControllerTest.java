package io.github.nicopolazzi.keepmygrind.controller;

import static java.util.Arrays.asList;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

}
