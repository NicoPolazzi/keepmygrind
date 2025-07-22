package io.github.nicopolazzi.keepmygrind.factory;

import org.hibernate.SessionFactory;

import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;
import io.github.nicopolazzi.keepmygrind.repository.GrindProfileRepository;
import io.github.nicopolazzi.keepmygrind.repository.sql.CoffeeSqlRepository;
import io.github.nicopolazzi.keepmygrind.repository.sql.GrindProfileSqlRepository;

public class SqlRepositoryFactory implements RepositoryFactory {

    SessionFactory sessionFactory;

    public SqlRepositoryFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public CoffeeRepository makeCoffeeRepository() {
        return new CoffeeSqlRepository(sessionFactory);
    }

    @Override
    public GrindProfileRepository makeGrindProfileRepository() {
        return new GrindProfileSqlRepository(sessionFactory);
    }

}
