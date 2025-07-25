package io.github.nicopolazzi.keepmygrind.repository.sql;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.model.GrindProfile;
import io.github.nicopolazzi.keepmygrind.repository.GrindProfileRepository;

class GrindProfileSqlRepositoryTest {

    private static final String GRINDPROFILE_FIXTURE_1_ID = "1";
    private static final String GRINDPROFILE_FIXTURE_1_BREW = "V60";
    private static final int GRINDPROFILE_FIXTURE_1_BEANS_GRAMS = 18;
    private static final int GRINDPROFILE_FIXTURE_1_WATER_MILLILITERS = 250;
    private static final int GRINDPROFILE_FIXTURE_1_CLICKS = 60;
    private static final String GRINDPROFILE_FIXTURE_2_ID = "2";
    private static final String GRINDPROFILE_FIXTURE_2_BREW = "espresso";
    private static final int GRINDPROFILE_FIXTURE_2_BEANS_GRAMS = 9;
    private static final int GRINDPROFILE_FIXTURE_2_WATER_MILLILITERS = 50;
    private static final int GRINDPROFILE_FIXTURE_2_CLICKS = 20;

    private static SessionFactory sessionFactory;

    private GrindProfileRepository grindProfileRepository;

    @BeforeAll
    static void setupSessionFactory() {
        sessionFactory = new Configuration().addAnnotatedClass(GrindProfile.class).addAnnotatedClass(Coffee.class)
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
        grindProfileRepository = new GrindProfileSqlRepository(sessionFactory);
    }

    @Test
    void testFindAllWhenDatabaseIsEmpty() {
        assertThat(grindProfileRepository.findAll()).isEmpty();
    }

    @Test
    void testFindAllWhenDatabaseIsNotEmpty() {
        var coffee = new Coffee("1", "test", "test");
        var profile1 = new GrindProfile(GRINDPROFILE_FIXTURE_1_ID, coffee, GRINDPROFILE_FIXTURE_1_BREW,
                GRINDPROFILE_FIXTURE_1_BEANS_GRAMS, GRINDPROFILE_FIXTURE_1_WATER_MILLILITERS,
                GRINDPROFILE_FIXTURE_1_CLICKS);
        var profile2 = new GrindProfile(GRINDPROFILE_FIXTURE_2_ID, coffee, GRINDPROFILE_FIXTURE_2_BREW,
                GRINDPROFILE_FIXTURE_2_BEANS_GRAMS, GRINDPROFILE_FIXTURE_2_WATER_MILLILITERS,
                GRINDPROFILE_FIXTURE_2_CLICKS);
        coffee.addGrindProfile(profile1);
        coffee.addGrindProfile(profile2);

        sessionFactory.inTransaction(session -> {
            session.persist(coffee);
        });

        List<GrindProfile> grindProfiles = grindProfileRepository.findAll();
        assertThat(grindProfiles).containsExactly(profile1, profile2);
    }

    @Test
    void testFindByIdNotFound() {
        assertThat(grindProfileRepository.findById(GRINDPROFILE_FIXTURE_1_ID)).isEmpty();
    }

    @Test
    void testFindByIdFound() {
        var coffee = new Coffee("1", "test", "test");
        var profile1 = new GrindProfile(GRINDPROFILE_FIXTURE_1_ID, coffee, GRINDPROFILE_FIXTURE_1_BREW,
                GRINDPROFILE_FIXTURE_1_BEANS_GRAMS, GRINDPROFILE_FIXTURE_1_WATER_MILLILITERS,
                GRINDPROFILE_FIXTURE_1_CLICKS);
        var profile2 = new GrindProfile(GRINDPROFILE_FIXTURE_2_ID, coffee, GRINDPROFILE_FIXTURE_2_BREW,
                GRINDPROFILE_FIXTURE_2_BEANS_GRAMS, GRINDPROFILE_FIXTURE_2_WATER_MILLILITERS,
                GRINDPROFILE_FIXTURE_2_CLICKS);
        coffee.addGrindProfile(profile1);
        coffee.addGrindProfile(profile2);
        sessionFactory.inTransaction(session -> session.persist(coffee));
        assertThat(grindProfileRepository.findById(GRINDPROFILE_FIXTURE_2_ID)).isEqualTo(Optional.of(profile2));
    }

    @Test
    void testSave() {
        var coffee = new Coffee("1", "test", "test");
        var profile = new GrindProfile(GRINDPROFILE_FIXTURE_1_ID, coffee, GRINDPROFILE_FIXTURE_1_BREW,
                GRINDPROFILE_FIXTURE_1_BEANS_GRAMS, GRINDPROFILE_FIXTURE_1_WATER_MILLILITERS,
                GRINDPROFILE_FIXTURE_1_CLICKS);
        sessionFactory.inTransaction(session -> session.persist(coffee));
        grindProfileRepository.save(profile);
        GrindProfile retrivedGrindProfile = sessionFactory
                .fromSession(session -> session.find(GrindProfile.class, GRINDPROFILE_FIXTURE_1_ID));
        assertThat(retrivedGrindProfile).isEqualTo(profile);
    }

    @Test
    void testDelete() {
        var coffee = new Coffee("1", "test", "test");
        var profile = new GrindProfile(GRINDPROFILE_FIXTURE_1_ID, coffee, GRINDPROFILE_FIXTURE_1_BREW,
                GRINDPROFILE_FIXTURE_1_BEANS_GRAMS, GRINDPROFILE_FIXTURE_1_WATER_MILLILITERS,
                GRINDPROFILE_FIXTURE_1_CLICKS);
        coffee.addGrindProfile(profile);
        sessionFactory.inTransaction(session -> session.persist(coffee));
        grindProfileRepository.delete(GRINDPROFILE_FIXTURE_1_ID);
        GrindProfile retrivedProfile = sessionFactory
                .fromSession(session -> session.find(GrindProfile.class, GRINDPROFILE_FIXTURE_1_ID));
        assertThat(retrivedProfile).isNull();
    }

}
