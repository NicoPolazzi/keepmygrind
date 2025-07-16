package io.github.nicopolazzi.keepmygrind.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
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
import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.model.GrindProfile;
import io.github.nicopolazzi.keepmygrind.repository.CoffeeRepository;
import io.github.nicopolazzi.keepmygrind.repository.sql.CoffeeSqlRepository;

@RunWith(GUITestRunner.class)
public class CoffeeSwingViewSqlIT extends AssertJSwingJUnitTestCase {

    private FrameFixture window;
    private CoffeeSwingView coffeeSwingView;

    private static SessionFactory sessionFactory;

    private CoffeeRepository coffeeRepository;
    private CoffeeController coffeeController;

    @BeforeClass
    public static void setUpSessionFactory() {
        sessionFactory = new Configuration().addAnnotatedClass(Coffee.class).addAnnotatedClass(GrindProfile.class)
                .setProperty(AvailableSettings.JAKARTA_JDBC_URL, "jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1")
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
