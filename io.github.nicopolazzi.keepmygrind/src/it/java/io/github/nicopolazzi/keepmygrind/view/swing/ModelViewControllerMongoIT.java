package io.github.nicopolazzi.keepmygrind.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import io.github.nicopolazzi.keepmygrind.controller.CoffeeController;
import io.github.nicopolazzi.keepmygrind.controller.GrindProfileController;
import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;
import io.github.nicopolazzi.keepmygrind.repository.GrindProfileRepository;
import io.github.nicopolazzi.keepmygrind.repository.mongo.CoffeeMongoRepository;
import io.github.nicopolazzi.keepmygrind.repository.mongo.GrindProfileMongoRepository;

@RunWith(GUITestRunner.class)
public class ModelViewControllerMongoIT extends AssertJSwingJUnitTestCase {

    @ClassRule
    public static final MongoDBContainer mongo = new MongoDBContainer("mongo:5");

    private MongoClient mongoClient;

    private FrameFixture window;
    private CoffeeController coffeeController;
    private GrindProfileController grindProfileController;
    private CoffeeRepository coffeeRepository;
    private GrindProfileRepository grindProfileRepository;

    @Override
    protected void onSetUp() throws Exception {
        mongoClient = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getFirstMappedPort()));
        coffeeRepository = new CoffeeMongoRepository(mongoClient);
        grindProfileRepository = new GrindProfileMongoRepository(mongoClient);
        coffeeRepository.findAll().forEach(coffee -> coffeeRepository.delete(coffee.getId()));
        grindProfileRepository.findAll().forEach(grindProfile -> grindProfileRepository.delete(grindProfile.getId()));

        window = new FrameFixture(robot(), GuiActionRunner.execute(() -> {
            CoffeeSwingView coffeeView = new CoffeeSwingView();
            GrindProfileSwingView grindProfileView = new GrindProfileSwingView();

            coffeeController = new CoffeeController(coffeeRepository, coffeeView, grindProfileView);
            grindProfileController = new GrindProfileController(grindProfileRepository, grindProfileView,
                    coffeeRepository);

            coffeeView.setCoffeeController(coffeeController);
            grindProfileView.setGrindProfileController(grindProfileController);
            return new KeepMyGrindSwingView(coffeeView, grindProfileView);
        }));
        window.show();
    }

    @Override
    protected void onTearDown() {
        mongoClient.close();
    }

    @Test
    public void testAddCoffee() {
        window.button(JButtonMatcher.withText("Coffee")).click();
        window.textBox("idTextBox").enterText("1");
        window.textBox("originTextBox").enterText("test");
        window.textBox("processTextBox").enterText("test");
        window.button(JButtonMatcher.withText("Add").andShowing()).click();
        assertThat(coffeeRepository.findById("1")).contains(new Coffee("1", "test", "test"));
    }

    @Test
    public void testDeleteCoffee() {
        coffeeRepository.save(new Coffee("99", "existing", "existing"));
        GuiActionRunner.execute(() -> coffeeController.allCoffees());
        window.button(JButtonMatcher.withText("Coffee")).click();
        window.list().selectItem(0);
        window.button(JButtonMatcher.withText("Delete Selected")).click();
        assertThat(coffeeRepository.findById("99")).isNotPresent();
    }

}
