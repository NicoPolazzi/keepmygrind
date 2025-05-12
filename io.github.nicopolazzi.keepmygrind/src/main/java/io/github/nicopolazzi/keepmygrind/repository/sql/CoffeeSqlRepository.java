package io.github.nicopolazzi.keepmygrind.repository.sql;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;

import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;

public class CoffeeSqlRepository implements CoffeeRepository {
    private SessionFactory sessionFactory;

    public CoffeeSqlRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Coffee> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Coffee> findById(String id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public void save(Coffee coffee) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub

    }

}
