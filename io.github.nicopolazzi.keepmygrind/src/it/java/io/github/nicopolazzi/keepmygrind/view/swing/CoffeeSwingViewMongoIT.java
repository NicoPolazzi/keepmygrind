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
import io.github.nicopolazzi.keepmygrind.controller.CoffeeController;
import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;
import io.github.nicopolazzi.keepmygrind.repository.mongo.CoffeeMongoRepository;

@RunWith(GUITestRunner.class)
public class CoffeeSwingViewMongoIT extends AssertJSwingJUnitTestCase {

    private FrameFixture window;
    private CoffeeSwingView coffeeSwingView;

    private static MongoServer server;
    private static InetSocketAddress serverAddress;

    private MongoClient client;
    private CoffeeRepository coffeeRepository;
    private CoffeeController coffeeController;

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
        coffeeRepository = new CoffeeMongoRepository(client);
        coffeeRepository.findAll().forEach(coffee -> coffeeRepository.delete(coffee.getId()));

        JFrame frame = GuiActionRunner.execute(() -> {
            JFrame f = new JFrame();
            coffeeSwingView = new CoffeeSwingView();
            GrindProfileSwingView grindProfileSwingView = new GrindProfileSwingView();
            coffeeController = new CoffeeController(coffeeRepository, coffeeSwingView, grindProfileSwingView);
            coffeeSwingView.setCoffeeController(coffeeController);
            f.setContentPane(coffeeSwingView);
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
    public void testAllCoffees() {
        var coffee1 = new Coffee("1", "test1", "test1");
        var coffee2 = new Coffee("2", "test2", "test2");
        coffeeRepository.save(coffee1);
        coffeeRepository.save(coffee2);
        GuiActionRunner.execute(() -> coffeeController.allCoffees());
        assertThat(window.list().contents()).containsExactly(coffee1.toString(), coffee2.toString());
    }

    @Test
    @GUITest
    public void testAddButtonSuccess() {
        window.textBox("idTextBox").enterText("1");
        window.textBox("originTextBox").enterText("test");
        window.textBox("processTextBox").enterText("test");
        window.button(JButtonMatcher.withText("Add")).click();
        assertThat(window.list().contents()).containsExactly("Coffee [id=1, origin=test, process=test]");
    }

    @Test
    @GUITest
    public void testAddButtonError() {
        coffeeRepository.save(new Coffee("1", "existing", "existing"));
        window.textBox("idTextBox").enterText("1");
        window.textBox("originTextBox").enterText("test2");
        window.textBox("processTextBox").enterText("test2");
        window.button(JButtonMatcher.withText("Add")).click();
        assertThat(window.list().contents()).isEmpty();
        window.label("errorMessageLabel")
                .requireText("Already existing coffee: Coffee [id=1, origin=existing, process=existing]");
    }

    @Test
    @GUITest
    public void testDeleteButtonSuccess() {
        GuiActionRunner.execute(() -> coffeeController.newCoffee(new Coffee("1", "existing", "existing")));
        window.list().selectItem(0);
        window.button(JButtonMatcher.withText("Delete Selected")).click();
        assertThat(window.list().contents()).isEmpty();
    }

    @Test
    @GUITest
    public void testDeleteButtonError() {
        // Manually adding a coffee to the view without interacting with the DB
        var coffee = new Coffee("1", "test", "test");
        GuiActionRunner.execute(() -> coffeeSwingView.getListCoffeesModel().addElement(coffee));
        window.list().selectItem(0);
        window.button(JButtonMatcher.withText("Delete Selected")).click();
        assertThat(window.list().contents()).containsExactly(coffee.toString());
        window.label("errorMessageLabel").requireText("Not existing coffee: Coffee [id=1, origin=test, process=test]");
    }
}
