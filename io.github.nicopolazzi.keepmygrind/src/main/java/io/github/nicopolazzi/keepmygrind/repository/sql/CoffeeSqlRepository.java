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
        return sessionFactory
                .fromSession(session -> session.createSelectionQuery("from Coffee", Coffee.class).getResultList());
    }

    @Override
    public Optional<Coffee> findById(String id) {
        return Optional.ofNullable(sessionFactory.fromSession(session -> session.find(Coffee.class, id)));
    }

    @Override
    public void save(Coffee coffee) {
        sessionFactory.inTransaction(session -> session.persist(coffee));
    }

    @Override
    public void delete(String id) {
        sessionFactory.inTransaction(session -> session.remove(session.find(Coffee.class, id)));
    }

}
