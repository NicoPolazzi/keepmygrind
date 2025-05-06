package io.github.nicopolazzi.keepmygrind.controller;

import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;
import io.github.nicopolazzi.keepmygrind.view.CoffeeView;

public class CoffeeController {

    private CoffeeRepository coffeeRepository;
    private CoffeeView coffeeView;

    public CoffeeController(CoffeeRepository coffeeRepository, CoffeeView coffeeView) {
        this.coffeeRepository = coffeeRepository;
        this.coffeeView = coffeeView;
    }

    public void allCoffees() {
        coffeeView.showAllCoffees(coffeeRepository.findAll());
    }

}
