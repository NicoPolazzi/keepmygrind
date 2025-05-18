package io.github.nicopolazzi.keepmygrind.repository.sql;

import static org.assertj.core.api.Assertions.assertThat;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.nicopolazzi.keepmygrind.model.GrindProfile;
import io.github.nicopolazzi.keepmygrind.repository.GrindProfileRepository;

class GrindProfileSqlRepositoryTest {

    private static SessionFactory sessionFactory;

    @BeforeAll
    static void setupSessionFactory() {
        sessionFactory = new Configuration().addAnnotatedClass(GrindProfile.class)
                .setProperty(AvailableSettings.JAKARTA_JDBC_URL, "jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1")
                .setProperty(AvailableSettings.JAKARTA_HBM2DDL_DATABASE_ACTION, Action.ACTION_CREATE_THEN_DROP)
                .buildSessionFactory();
    }

    @AfterAll
    static void tearDown() {
        sessionFactory.close();
    }

    @BeforeEach
    void clearDatabase() {
        sessionFactory.getSchemaManager().truncateMappedObjects();
    }

    @Test
    void testFindAllWhenDatabaseIsEmpty() {
        sessionFactory.inTransaction(session -> {
            GrindProfileRepository coffeeRepository = new GrindProfileSqlRepository(session);
            assertThat(coffeeRepository.findAll()).isEmpty();
        });
    }

}
