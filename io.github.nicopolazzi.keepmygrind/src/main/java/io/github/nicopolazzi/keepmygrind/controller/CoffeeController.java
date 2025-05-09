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
        coffeeRepository.findById(coffee.getId())
                .ifPresentOrElse(existing -> coffeeView.showExistingCoffeeError(existing), () -> {
                    coffeeRepository.save(coffee);
                    coffeeView.coffeeAdded(coffee);
                });

    }

    public void deleteCoffee(Coffee coffee) {
        coffeeRepository.findById(coffee.getId()).ifPresentOrElse(existing -> {
            coffeeRepository.delete(existing.getId());
            coffeeView.coffeeDeleted(existing);
        }, () -> coffeeView.showNotExistingCoffeeError(coffee));
    }

}
