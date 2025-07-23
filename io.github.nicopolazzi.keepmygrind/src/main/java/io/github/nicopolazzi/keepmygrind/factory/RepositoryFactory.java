package io.github.nicopolazzi.keepmygrind.factory;

import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;
import io.github.nicopolazzi.keepmygrind.repository.GrindProfileRepository;

public interface RepositoryFactory {

    CoffeeRepository makeCoffeeRepository();

    GrindProfileRepository makeGrindProfileRepository();

}
