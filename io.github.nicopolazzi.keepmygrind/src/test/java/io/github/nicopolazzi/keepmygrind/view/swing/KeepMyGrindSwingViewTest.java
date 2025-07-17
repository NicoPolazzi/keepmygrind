package io.github.nicopolazzi.keepmygrind.view.swing;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GUITestRunner.class)
public class KeepMyGrindSwingViewTest extends AssertJSwingJUnitTestCase {
    private FrameFixture window;

    @Override
    protected void onSetUp() throws Exception {
        window = new FrameFixture(robot(), GuiActionRunner.execute(KeepMyGrindSwingView::new));
        window.show();
    }

    @Test
    @GUITest
    public void testControlsInitialStates() {
        window.button(JButtonMatcher.withText("Coffee")).requireEnabled();
        window.button(JButtonMatcher.withText("Grind Profile")).requireEnabled();
    }

    @Test
    public void testCoffeeButtonShowsCoffeePanel() {
        // andShowing() is needed because both Coffee and GrindProfile have such graphic
        // components
        window.button(JButtonMatcher.withText("Coffee")).click();

        window.label(JLabelMatcher.withText("id").andShowing());
        window.textBox("idTextBox").requireEnabled();
        window.label(JLabelMatcher.withText("origin"));
        window.textBox("originTextBox").requireEnabled();
        window.label(JLabelMatcher.withText("process"));
        window.textBox("processTextBox").requireEnabled();
        window.button(JButtonMatcher.withText("Add").andShowing()).requireDisabled();
        window.list("coffeeList");
        window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();
        window.label("errorMessageLabel").requireText(" ");
    }

    @Test
    public void testGrindProfileButtonShowsGrindProfilePanel() {
        window.button(JButtonMatcher.withText("Grind Profile")).click();

        window.label(JLabelMatcher.withText("id").andShowing());
        window.textBox("idTextBox").requireEnabled();
        window.label(JLabelMatcher.withText("coffee"));
        window.comboBox("coffeeComboBox").requireEnabled();
        window.label(JLabelMatcher.withText("brew"));
        window.textBox("brewTextBox").requireEnabled();
        window.label(JLabelMatcher.withText("grams"));
        window.textBox("gramsTextBox").requireEnabled();
        window.label(JLabelMatcher.withText("water"));
        window.textBox("waterTextBox").requireEnabled();
        window.label(JLabelMatcher.withText("clicks"));
        window.textBox("clicksTextBox").requireEnabled();
        window.button(JButtonMatcher.withText("Add").andShowing()).requireDisabled();
        window.list("grindProfileList");
        window.button(JButtonMatcher.withText("Delete Selected Profile")).requireDisabled();
        window.label("errorMessageLabel").requireText(" ");
    }

}
