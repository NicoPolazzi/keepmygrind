package io.github.nicopolazzi.keepmygrind.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import io.github.nicopolazzi.keepmygrind.controller.GrindProfileController;
import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.model.GrindProfile;
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;
import io.github.nicopolazzi.keepmygrind.repository.GrindProfileRepository;
import io.github.nicopolazzi.keepmygrind.repository.mongo.CoffeeMongoRepository;
import io.github.nicopolazzi.keepmygrind.repository.mongo.GrindProfileMongoRepository;

@RunWith(GUITestRunner.class)
public class GrindProfileSwingViewMongoIT extends AssertJSwingJUnitTestCase {

    private FrameFixture window;

    private static MongoServer server;
    private static InetSocketAddress serverAddress;

    private MongoClient client;

    private GrindProfileSwingView grindProfileView;
    private GrindProfileController grindProfileController;

    private GrindProfileRepository grindProfileRepository;

    private CoffeeRepository coffeeRepository;

    @BeforeClass
    public static void setUpServer() {
        server = new MongoServer(new MemoryBackend());
        serverAddress = server.bind();
    }

    @AfterClass
    public static void shutdownServer() {
        server.shutdown();
    }

    @Override
    protected void onSetUp() throws Exception {
        client = new MongoClient(new ServerAddress(serverAddress));
        grindProfileRepository = new GrindProfileMongoRepository(client);
        coffeeRepository = new CoffeeMongoRepository(client);
        grindProfileRepository.findAll().forEach(grindProfile -> grindProfileRepository.delete(grindProfile.getId()));
        coffeeRepository.delete("1");

        JFrame frame = GuiActionRunner.execute(() -> {
            JFrame f = new JFrame();
            grindProfileView = new GrindProfileSwingView();
            grindProfileController = new GrindProfileController(grindProfileRepository, grindProfileView,
                    coffeeRepository);
            grindProfileView.setGrindProfileController(grindProfileController);
            f.setContentPane(grindProfileView);
            f.pack();
            f.setSize(600, 500);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            return f;
        });

        window = new FrameFixture(robot(), frame);
        window.show();
    }

    @Override
    protected void onTearDown() throws Exception {
        client.close();
    }

    @Test
    @GUITest
    public void testAllGrindProfiles() {
        var grindProfile1 = new GrindProfile("1", new Coffee("1", "test", "test"), "test", 14.2, 100, 30);
        var grindProfile2 = new GrindProfile("2", new Coffee("1", "test", "test"), "test", 14.2, 100, 30);

        grindProfileRepository.save(grindProfile1);
        grindProfileRepository.save(grindProfile2);
        GuiActionRunner.execute(() -> grindProfileController.allGrindProfiles());

        assertThat(window.list().contents()).containsExactly(grindProfile1.toString(), grindProfile2.toString());
    }

    @Test
    @GUITest
    public void testAddButtonSuccess() {
        window.textBox("idTextBox").enterText("1");
        GuiActionRunner.execute(() -> {
            grindProfileView.getComboBoxCoffeeModel().addElement(new Coffee("1", "test", "test"));
            coffeeRepository.save(new Coffee("1", "test", "test"));
        });
        window.comboBox("coffeeComboBox").selectItem(0);
        window.textBox("brewTextBox").enterText("test");
        window.textBox("gramsTextBox").enterText("15");
        window.textBox("waterTextBox").enterText("150");
        window.textBox("clicksTextBox").enterText("30");
        window.button(JButtonMatcher.withText("Add")).click();
        assertThat(window.list().contents()).containsExactly(
                "GrindProfile [id=1, coffeeId=1, brew=test, beanGrams=15.0, waterMilliliters=150.0, clicks=30]");
    }

    @Test
    @GUITest
    public void testAddButtonErrorWhenCoffeeNotFound() {
        // Here the coffee isn't present in the repository when we try to add the grind
        // profile
        window.textBox("idTextBox").enterText("1");
        GuiActionRunner
                .execute(() -> grindProfileView.getComboBoxCoffeeModel().addElement(new Coffee("1", "test", "test")));
        window.comboBox("coffeeComboBox").selectItem(0);
        window.textBox("brewTextBox").enterText("test");
        window.textBox("gramsTextBox").enterText("15");
        window.textBox("waterTextBox").enterText("150");
        window.textBox("clicksTextBox").enterText("30");
        window.button(JButtonMatcher.withText("Add")).click();
        assertThat(window.list().contents()).isEmpty();
        window.label("errorMessageLabel").requireText("Cannot find a grind profile for the coffee with id: 1");
    }

    @Test
    @GUITest
    public void testAddButtonErrorWhenGrindProfileIsAlreadyPresent() {
        var coffee = new Coffee("1", "test", "test");
        grindProfileRepository.save(new GrindProfile("1", coffee, "test", 14.2, 100, 30));

        window.textBox("idTextBox").enterText("1");
        GuiActionRunner.execute(() -> {
            grindProfileView.getComboBoxCoffeeModel().addElement(coffee);
            coffeeRepository.save(new Coffee("1", "test", "test"));
        });
        window.comboBox("coffeeComboBox").selectItem(0);
        window.textBox("brewTextBox").enterText("test");
        window.textBox("gramsTextBox").enterText("15");
        window.textBox("waterTextBox").enterText("150");
        window.textBox("clicksTextBox").enterText("30");
        window.button(JButtonMatcher.withText("Add")).click();

        assertThat(window.list().contents()).isEmpty();
        window.label("errorMessageLabel").requireText(
                "Already existing grind profile: GrindProfile [id=1, coffeeId=1, brew=test, beanGrams=14.2, waterMilliliters=100.0, clicks=30]");
    }

    @Test
    @GUITest
    public void testDeleteButtonSuccess() {
        var coffee = new Coffee("1", "test", "test");
        GuiActionRunner.execute(() -> {
            coffeeRepository.save(coffee);
            grindProfileController.newGrindProfile(new GrindProfile("1", coffee, "test", 14.2, 100, 30));
        });

        window.list().selectItem(0);
        window.button(JButtonMatcher.withText("Delete Selected Profile")).click();

        assertThat(window.list().contents()).isEmpty();
    }

    @Test
    @GUITest
    public void testDeleteButtonError() {
        var grindProfile = new GrindProfile("1", new Coffee("1", "test", "test"), "test", 14.2, 100, 30);
        GuiActionRunner.execute(() -> grindProfileView.getListGrindProfileModel().addElement(grindProfile));
        window.list().selectItem(0);
        window.button(JButtonMatcher.withText("Delete Selected Profile")).click();
        assertThat(window.list().contents()).containsExactly(grindProfile.toString());
        window.label("errorMessageLabel").requireText(
                "Not existing grind profile: GrindProfile [id=1, coffeeId=1, brew=test, beanGrams=14.2, waterMilliliters=100.0, clicks=30]");
    }
}
