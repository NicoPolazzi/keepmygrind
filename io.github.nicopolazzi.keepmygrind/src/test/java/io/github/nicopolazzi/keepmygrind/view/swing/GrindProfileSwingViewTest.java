package io.github.nicopolazzi.keepmygrind.view.swing;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

public class GrindProfileSwingViewTest extends AssertJSwingJUnitTestCase {
    private FrameFixture window;
    private GrindProfileSwingView grindProfileView;

    @Override
    protected void onSetUp() throws Exception {
        GuiActionRunner.execute(() -> {
            grindProfileView = new GrindProfileSwingView();
            return grindProfileView;
        });

        JFrame frame = GuiActionRunner.execute(() -> {
            JFrame f = new JFrame();
            f.setContentPane(grindProfileView);
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
        window.label(JLabelMatcher.withText("coffee"));
        window.textBox("coffeeTextBox").requireEnabled();
        window.label(JLabelMatcher.withText("brew"));
        window.textBox("brewTextBox").requireEnabled();
        window.label(JLabelMatcher.withText("grams"));
        window.textBox("gramsTextBox").requireEnabled();
        window.label(JLabelMatcher.withText("water"));
        window.textBox("waterTextBox").requireEnabled();
        window.label(JLabelMatcher.withText("clicks"));
        window.textBox("clicksTextBox").requireEnabled();
        window.button(JButtonMatcher.withText("Add")).requireDisabled();
        window.list("grindProfileList");
        window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();
        window.label("errorMessageLabel").requireText(" ");
    }

    @Test
    public void testWhenGrindProfileInformationAreNonEmptyThenAddButtonShouldBeEnabled() {
        window.textBox("idTextBox").enterText("1");
        window.textBox("coffeeTextBox").enterText("test");
        window.textBox("brewTextBox").enterText("test");
        window.textBox("gramsTextBox").enterText("15");
        window.textBox("waterTextBox").enterText("test");
        window.textBox("clicksTextBox").enterText("30");
        window.button(JButtonMatcher.withText("Add")).requireEnabled();
    }

}
