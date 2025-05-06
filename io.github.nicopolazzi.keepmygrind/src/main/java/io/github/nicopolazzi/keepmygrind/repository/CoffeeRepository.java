package io.github.nicopolazzi.keepmygrind.repository;

import java.util.List;

import io.github.nicopolazzi.keepmygrind.model.Coffee;

public interface CoffeeRepository {

    List<Coffee> findAll();

}
