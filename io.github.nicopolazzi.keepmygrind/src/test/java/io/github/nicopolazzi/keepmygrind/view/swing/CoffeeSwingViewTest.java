package io.github.nicopolazzi.keepmygrind.view.swing;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

import io.github.nicopolazzi.keepmygrind.controller.CoffeeController;
import io.github.nicopolazzi.keepmygrind.model.Coffee;

public class CoffeeSwingViewTest extends AssertJSwingJUnitTestCase {
    private FrameFixture window;
    private CoffeeSwingView coffeeSwingView;

    @Mock
    private CoffeeController coffeController;

    private AutoCloseable closeable;

    @Override
    protected void onSetUp() throws Exception {
        closeable = MockitoAnnotations.openMocks(this);
        GuiActionRunner.execute(() -> {
            coffeeSwingView = new CoffeeSwingView();
            coffeeSwingView.setCoffeeController(coffeController);
            return coffeeSwingView;
        });
        JFrame frame = GuiActionRunner.execute(() -> {
            JFrame f = new JFrame();
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
        closeable.close();
    }

    @Test
    @GUITest
    public void testControlsInitialStates() {
        window.label(JLabelMatcher.withText("id"));
        window.textBox("idTextBox").requireEnabled();
        window.label(JLabelMatcher.withText("origin"));
        window.textBox("originTextBox").requireEnabled();
        window.label(JLabelMatcher.withText("process"));
        window.textBox("processTextBox").requireEnabled();
        window.button(JButtonMatcher.withText("Add")).requireDisabled();
        window.list("coffeeList");
        window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();
        window.label("errorMessageLabel").requireText(" ");
    }

    @Test
    public void testWhenCoffeeInformationAreNonEmptyThenAddButtonShouldBeEnabled() {
        window.textBox("idTextBox").enterText("1");
        window.textBox("originTextBox").enterText("test");
        window.textBox("processTextBox").enterText("test");
        window.button(JButtonMatcher.withText("Add")).requireEnabled();
    }

    @Test
    public void testWhenSomeInformationIsBlankThenAddButtonShouldBeDisabled() {
        JTextComponentFixture idTextBox = window.textBox("idTextBox");
        JTextComponentFixture originTextBox = window.textBox("originTextBox");
        JTextComponentFixture processTextBox = window.textBox("processTextBox");

        idTextBox.enterText("1");
        originTextBox.enterText("test");
        processTextBox.enterText(" ");
        window.button(JButtonMatcher.withText("Add")).requireDisabled();

        idTextBox.setText("");
        originTextBox.setText("");
        processTextBox.setText("");

        idTextBox.enterText(" ");
        originTextBox.enterText("test");
        processTextBox.enterText("test");
        window.button(JButtonMatcher.withText("Add")).requireDisabled();

        idTextBox.setText("");
        originTextBox.setText("");
        processTextBox.setText("");

        idTextBox.enterText("1");
        originTextBox.enterText(" ");
        processTextBox.enterText("test");
        window.button(JButtonMatcher.withText("Add")).requireDisabled();
    }

    @Test
    public void testDeleteButtonShouldBeEnabledOnlyWhenACoffeeIsSelected() {
        GuiActionRunner
                .execute(() -> coffeeSwingView.getListCoffeesModel().addElement(new Coffee("1", "test", "test")));
        window.list("coffeeList").selectItem(0);
        JButtonFixture deleteButton = window.button(JButtonMatcher.withText("Delete Selected"));
        deleteButton.requireEnabled();
        window.list("coffeeList").clearSelection();
        deleteButton.requireDisabled();
    }

    @Test
    public void testsShowAllCoffeesShouldAddCoffeeInformationToTheList() {
        var coffee1 = new Coffee("1", "test1", "test1");
        var coffee2 = new Coffee("2", "test2", "test2");
        GuiActionRunner.execute(() -> coffeeSwingView.showAllCoffees(asList(coffee1, coffee2)));
        String[] listContents = window.list("coffeeList").contents();
        assertThat(listContents).containsExactly(coffee1.toString(), coffee2.toString());
    }

    @Test
    public void testShowExistingCoffeeErrorShouldShowTheMessageInTheErrorLabel() {
        var coffee = new Coffee("1", "test", "test");
        GuiActionRunner.execute(() -> coffeeSwingView.showExistingCoffeeError(coffee));
        window.label("errorMessageLabel").requireText("Already existing coffee: " + coffee);
    }

    @Test
    public void testShowNotExistingCoffeeErrorShouldShowTheMessageInTheErrorLabel() {
        var coffee = new Coffee("1", "test", "test");
        GuiActionRunner.execute(() -> coffeeSwingView.showNotExistingCoffeeError(coffee));
        window.label("errorMessageLabel").requireText("Not existing coffee: " + coffee);
    }

    @Test
    public void testCoffeeAddedShouldAddTheCoffeeToTheListAndResetTheErrorLabel() {
        var coffee = new Coffee("1", "test", "test");
        GuiActionRunner.execute(() -> coffeeSwingView.coffeeAdded(new Coffee("1", "test", "test")));
        String[] listContents = window.list("coffeeList").contents();
        assertThat(listContents).containsExactly(coffee.toString());
        window.label("errorMessageLabel").requireText(" ");
    }

    @Test
    public void testCoffeeRemovedShouldRemoveTheCoffeeFromTheListAndResetTheErrorLabel() {
        var coffee1 = new Coffee("1", "test1", "test1");
        var coffee2 = new Coffee("2", "test2", "test2");
        GuiActionRunner.execute(() -> {
            DefaultListModel<Coffee> listCoffeesModel = coffeeSwingView.getListCoffeesModel();
            listCoffeesModel.addElement(coffee1);
            listCoffeesModel.addElement(coffee2);
        });

        GuiActionRunner.execute(() -> coffeeSwingView.coffeeRemoved(new Coffee("1", "test1", "test1")));

        String[] listContents = window.list("coffeeList").contents();
        assertThat(listContents).containsExactly(coffee2.toString());
        window.label("errorMessageLabel").requireText(" ");
    }

    @Test
    public void testAddButtonShouldDelegateToCoffeeControllerNewCoffee() {
        window.textBox("idTextBox").enterText("1");
        window.textBox("originTextBox").enterText("test");
        window.textBox("processTextBox").enterText("test");
        window.button(JButtonMatcher.withText("Add")).click();
        verify(coffeController).newCoffee(new Coffee("1", "test", "test"));
    }
}
