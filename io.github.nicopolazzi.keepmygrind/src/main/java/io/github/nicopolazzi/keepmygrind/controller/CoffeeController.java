package io.github.nicopolazzi.keepmygrind.controller;

import io.github.nicopolazzi.keepmygrind.model.Coffee;
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

    public void newCoffee(Coffee coffee) {
        if (coffeeRepository.findById(coffee.getId()).isEmpty()) {
            coffeeRepository.save(coffee);
            coffeeView.coffeeAdded(coffee);
        }
    }

}
