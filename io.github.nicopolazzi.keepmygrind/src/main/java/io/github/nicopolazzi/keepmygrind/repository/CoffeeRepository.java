package io.github.nicopolazzi.keepmygrind.repository;

import java.util.List;
import java.util.Optional;

import io.github.nicopolazzi.keepmygrind.model.Coffee;

public interface CoffeeRepository {

    List<Coffee> findAll();

    Optional<Coffee> findById(String string);

    void save(Coffee coffee);

    void delete(Coffee coffee);

}
