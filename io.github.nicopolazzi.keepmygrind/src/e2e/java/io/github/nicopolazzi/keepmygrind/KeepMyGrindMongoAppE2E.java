package io.github.nicopolazzi.keepmygrind;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;

import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.model.GrindProfile;

@RunWith(GUITestRunner.class)
public class KeepMyGrindMongoAppE2E extends AssertJSwingJUnitTestCase {

    private static final String DB_NAME = "keepmygrind";
    private static final String COFFEE_COLLECTION_NAME = "coffee";
    private static final String GRINDPROFILE_COLLECTION_NAME = "grindprofile";

    private static final String COFFEE_FIXTURE_1_ID = "1";
    private static final String COFFEE_FIXTURE_1_ORIGIN = "origin1";
    private static final String COFFEE_FIXTURE_1_PROCESS = "process1";
    private static final String COFFEE_FIXTURE_2_ID = "2";
    private static final String COFFEE_FIXTURE_2_ORIGIN = "origin2";
    private static final String COFFEE_FIXTURE_2_PROCESS = "process2";

    private static final String GRINDPROFILE_FIXTURE_1_ID = "1";
    private static final String GRINDPROFILE_FIXTURE_1_BREW = "V60";
    private static final double GRINDPROFILE_FIXTURE_1_BEANS_GRAMS = 18.5;
    private static final double GRINDPROFILE_FIXTURE_1_WATER_MILLILITERS = 250;
    private static final int GRINDPROFILE_FIXTURE_1_CLICKS = 60;
    private static final String GRINDPROFILE_FIXTURE_2_ID = "2";
    private static final String GRINDPROFILE_FIXTURE_2_BREW = "espresso";
    private static final double GRINDPROFILE_FIXTURE_2_BEANS_GRAMS = 10.2;
    private static final double GRINDPROFILE_FIXTURE_2_WATER_MILLILITERS = 100;
    private static final int GRINDPROFILE_FIXTURE_2_CLICKS = 20;

    @ClassRule
    public static final MongoDBContainer mongo = new MongoDBContainer("mongo:5");

    private MongoClient mongoClient;
    private CodecRegistry pojoCodecRegistry;

    private FrameFixture window;
    private Coffee coffee;

    @Override
    protected void onSetUp() throws Exception {
        String containerIpAddress = mongo.getHost();
        Integer mappedPort = mongo.getFirstMappedPort();
        mongoClient = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getFirstMappedPort()));
        pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        mongoClient.getDatabase(DB_NAME).drop();

        coffee = new Coffee(COFFEE_FIXTURE_1_ID, COFFEE_FIXTURE_1_ORIGIN, COFFEE_FIXTURE_1_PROCESS);
        addCoffeeToDatabase(coffee);
        addCoffeeToDatabase(new Coffee(COFFEE_FIXTURE_2_ID, COFFEE_FIXTURE_2_ORIGIN, COFFEE_FIXTURE_2_PROCESS));
        addGrindProfileToDatabase(new GrindProfile(GRINDPROFILE_FIXTURE_1_ID, coffee, GRINDPROFILE_FIXTURE_1_BREW,
                GRINDPROFILE_FIXTURE_1_BEANS_GRAMS, GRINDPROFILE_FIXTURE_1_WATER_MILLILITERS,
                GRINDPROFILE_FIXTURE_1_CLICKS));
        addGrindProfileToDatabase(new GrindProfile(GRINDPROFILE_FIXTURE_2_ID, coffee, GRINDPROFILE_FIXTURE_2_BREW,
                GRINDPROFILE_FIXTURE_2_BEANS_GRAMS, GRINDPROFILE_FIXTURE_2_WATER_MILLILITERS,
                GRINDPROFILE_FIXTURE_2_CLICKS));

        application("io.github.nicopolazzi.keepmygrind.app.swing.KeepMyGrindSwingApp")
                .withArgs("--mongo-host=" + containerIpAddress, "--mongo-port=" + mappedPort.toString()).start();

        window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
            @Override
            protected boolean isMatching(JFrame component) {
                return "KeepMyGrind".equals(component.getTitle()) && component.isShowing();
            }
        }).using(robot());
    }

    @Override
    protected void onTearDown() throws Exception {
        mongoClient.close();
    }

    private void addCoffeeToDatabase(Coffee coffee) {
        mongoClient.getDatabase(DB_NAME).withCodecRegistry(pojoCodecRegistry)
                .getCollection(COFFEE_COLLECTION_NAME, Coffee.class).insertOne(coffee);
    }

    private void addGrindProfileToDatabase(GrindProfile grindProfile) {
        mongoClient.getDatabase(DB_NAME).withCodecRegistry(pojoCodecRegistry)
                .getCollection(GRINDPROFILE_COLLECTION_NAME, GrindProfile.class).insertOne(grindProfile);
    }

    @Test
    @GUITest
    public void testOnStartAllCoffeesAreShown() {
        window.button(JButtonMatcher.withText("Coffee")).click();
        assertThat(window.list().contents()).anySatisfy(
                e -> assertThat(e).contains(COFFEE_FIXTURE_1_ID, COFFEE_FIXTURE_1_ORIGIN, COFFEE_FIXTURE_1_PROCESS))
                .anySatisfy(e -> assertThat(e).contains(COFFEE_FIXTURE_2_ID, COFFEE_FIXTURE_2_ORIGIN,
                        COFFEE_FIXTURE_2_PROCESS));
    }

    @Test
    @GUITest
    public void testOnStartAllGrindProfilesAreShown() {
        window.button(JButtonMatcher.withText("Grind Profile")).click();
        assertThat(window.list().contents())
                .anySatisfy(e -> assertThat(e).contains(GRINDPROFILE_FIXTURE_1_ID, COFFEE_FIXTURE_1_ID,
                        GRINDPROFILE_FIXTURE_1_BREW, String.valueOf(GRINDPROFILE_FIXTURE_1_BEANS_GRAMS),
                        String.valueOf(GRINDPROFILE_FIXTURE_1_WATER_MILLILITERS),
                        String.valueOf(GRINDPROFILE_FIXTURE_1_CLICKS)))
                .anySatisfy(e -> assertThat(e).contains(GRINDPROFILE_FIXTURE_2_ID, COFFEE_FIXTURE_1_ID,
                        GRINDPROFILE_FIXTURE_2_BREW, String.valueOf(GRINDPROFILE_FIXTURE_2_BEANS_GRAMS),
                        String.valueOf(GRINDPROFILE_FIXTURE_2_WATER_MILLILITERS),
                        String.valueOf(GRINDPROFILE_FIXTURE_2_CLICKS)));
    }

}
