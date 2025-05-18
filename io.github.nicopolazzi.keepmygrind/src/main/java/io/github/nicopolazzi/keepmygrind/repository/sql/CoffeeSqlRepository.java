package io.github.nicopolazzi.keepmygrind.repository.sql;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;

import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;

public class CoffeeSqlRepository implements CoffeeRepository {
    private Session session;

    public CoffeeSqlRepository(Session session) {
        this.session = session;
    }

    @Override
    public List<Coffee> findAll() {
        return session.createSelectionQuery("from Coffee", Coffee.class).getResultList();
    }

    @Override
    public Optional<Coffee> findById(String id) {
        return Optional.ofNullable(session.find(Coffee.class, id));
    }

    @Override
    public void save(Coffee coffee) {
        session.persist(coffee);
    }

    @Override
    public void delete(String id) {
        session.remove(session.find(Coffee.class, id));
    }

}
