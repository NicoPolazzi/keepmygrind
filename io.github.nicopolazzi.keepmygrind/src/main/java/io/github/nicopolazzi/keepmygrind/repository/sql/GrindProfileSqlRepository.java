package io.github.nicopolazzi.keepmygrind.repository.sql;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;

import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.model.GrindProfile;
import io.github.nicopolazzi.keepmygrind.repository.GrindProfileRepository;

public class GrindProfileSqlRepository implements GrindProfileRepository {

    private final SessionFactory sessionFactory;

    public GrindProfileSqlRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<GrindProfile> findAll() {
        return sessionFactory.fromSession(
                session -> session.createSelectionQuery("from GrindProfile", GrindProfile.class).getResultList());
    }

    @Override
    public Optional<GrindProfile> findById(String id) {
        return sessionFactory.fromSession(session -> Optional.ofNullable(session.find(GrindProfile.class, id)));
    }

    @Override
    public void save(GrindProfile profile) {
        sessionFactory.inTransaction(session -> {
            Coffee managedCoffee = session.find(Coffee.class, profile.getCoffee().getId());
            if (managedCoffee == null)
                throw new IllegalArgumentException("Coffee must exist in DB");
            profile.setCoffee(managedCoffee);
            session.persist(profile);
        });

    }

    @Override
    public void delete(String id) {
        sessionFactory.inTransaction(session -> session.remove(session.find(GrindProfile.class, id)));
    }
}