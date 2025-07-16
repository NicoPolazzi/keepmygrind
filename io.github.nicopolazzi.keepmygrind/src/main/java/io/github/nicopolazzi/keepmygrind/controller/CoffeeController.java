package io.github.nicopolazzi.keepmygrind.controller;

import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;
import io.github.nicopolazzi.keepmygrind.view.CoffeeView;
import io.github.nicopolazzi.keepmygrind.view.GrindProfileView;

public class CoffeeController {

    private CoffeeRepository coffeeRepository;
    private CoffeeView coffeeView;
    private GrindProfileView grindProfileView;

    public CoffeeController(CoffeeRepository coffeeRepository, CoffeeView coffeeView,
            GrindProfileView grindProfileView) {
        this.coffeeRepository = coffeeRepository;
        this.coffeeView = coffeeView;
        this.grindProfileView = grindProfileView;
    }

    public void allCoffees() {
        coffeeView.showAllCoffees(coffeeRepository.findAll());
    }

    public void newCoffee(Coffee coffee) {
        coffeeRepository.findById(coffee.getId())
                .ifPresentOrElse(existing -> coffeeView.showExistingCoffeeError(existing), () -> {
                    coffeeRepository.save(coffee);
                    coffeeView.coffeeAdded(coffee);
                    grindProfileView.refreshCoffees(coffeeRepository.findAll());
                });
    }

    public void deleteCoffee(Coffee coffee) {
        coffeeRepository.findById(coffee.getId()).ifPresentOrElse(existing -> {
            coffeeRepository.delete(existing.getId());
            coffeeView.coffeeRemoved(existing);
        }, () -> coffeeView.showNotExistingCoffeeError(coffee));
    }

}
