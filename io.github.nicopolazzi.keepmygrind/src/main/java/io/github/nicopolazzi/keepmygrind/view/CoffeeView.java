package io.github.nicopolazzi.keepmygrind.view;

import java.util.List;

import io.github.nicopolazzi.keepmygrind.model.Coffee;

public interface CoffeeView {

    void showAllCoffees(List<Coffee> coffees);

    void coffeeAdded(Coffee coffee);

    void showExistingCoffeeError(Coffee existingCoffee);

    void coffeeDeleted(Coffee coffee);

    void showNotExistingCoffeeError(Coffee coffee);

}
