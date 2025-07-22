package io.github.nicopolazzi.keepmygrind.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.nicopolazzi.keepmygrind.controller.CoffeeController;
import io.github.nicopolazzi.keepmygrind.controller.GrindProfileController;
import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.model.GrindProfile;
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;
import io.github.nicopolazzi.keepmygrind.repository.GrindProfileRepository;
import io.github.nicopolazzi.keepmygrind.repository.sql.CoffeeSqlRepository;
import io.github.nicopolazzi.keepmygrind.repository.sql.GrindProfileSqlRepository;

@RunWith(GUITestRunner.class)
public class ModelViewControllerSqlIT extends AssertJSwingJUnitTestCase {

    private static SessionFactory sessionFactory;

    private FrameFixture window;
    private CoffeeController coffeeController;
    private GrindProfileController grindProfileController;
    private CoffeeRepository coffeeRepository;
    private GrindProfileRepository grindProfileRepository;

    @BeforeClass
    public static void setupSessionFactory() {
        sessionFactory = new Configuration().addAnnotatedClass(GrindProfile.class).addAnnotatedClass(Coffee.class)
                .setProperty(AvailableSettings.JAKARTA_JDBC_URL, "jdbc:tc:mysql:8:///keepmygrind")
                .setProperty(AvailableSettings.JAKARTA_HBM2DDL_DATABASE_ACTION, Action.ACTION_CREATE_THEN_DROP)
                .buildSessionFactory();
    }

    @AfterClass
    public static void tearDownSessionFactory() {
        sessionFactory.close();
    }

    @Override
    protected void onSetUp() throws Exception {
        coffeeRepository = new CoffeeSqlRepository(sessionFactory);
        grindProfileRepository = new GrindProfileSqlRepository(sessionFactory);
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

    @Test
    public void testAddGrindProfile() {
        GuiActionRunner.execute(() -> coffeeController.newCoffee(new Coffee("1", "test", "test")));
        window.button(JButtonMatcher.withText("Grind Profile")).click();
        window.textBox("idTextBox").enterText("1");
        window.comboBox("coffeeComboBox").selectItem(0);
        window.textBox("brewTextBox").enterText("test");
        window.textBox("gramsTextBox").enterText("14.2");
        window.textBox("waterTextBox").enterText("100");
        window.textBox("clicksTextBox").enterText("30");
        window.button(JButtonMatcher.withText("Add").andShowing()).click();
        assertThat(grindProfileRepository.findById("1"))
                .contains(new GrindProfile("1", new Coffee("1", "test", "test"), "test", 14.2, 100, 30));
    }

    @Test
    public void testDeleteGrindProfile() {
        GuiActionRunner.execute(() -> {
            coffeeController.newCoffee(new Coffee("1", "test", "test"));
            grindProfileRepository.save(new GrindProfile("99", new Coffee("1", "test", "test"), "test", 14.2, 100, 30));
            grindProfileController.allGrindProfiles();
        });

        window.button(JButtonMatcher.withText("Grind Profile")).click();
        window.list("grindProfileList").selectItem(0);
        window.button(JButtonMatcher.withText("Delete Selected Profile")).click();
        assertThat(grindProfileRepository.findById("99")).isNotPresent();
    }

}
