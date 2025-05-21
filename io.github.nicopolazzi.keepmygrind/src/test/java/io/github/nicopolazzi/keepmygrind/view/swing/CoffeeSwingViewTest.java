package io.github.nicopolazzi.keepmygrind.view.swing;

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

import io.github.nicopolazzi.keepmygrind.model.Coffee;

public class CoffeeSwingViewTest extends AssertJSwingJUnitTestCase {
    private FrameFixture window;
    private CoffeeSwingView coffeeSwingView;

    @Override
    protected void onSetUp() throws Exception {
        coffeeSwingView = GuiActionRunner.execute(CoffeeSwingView::new);
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
}
