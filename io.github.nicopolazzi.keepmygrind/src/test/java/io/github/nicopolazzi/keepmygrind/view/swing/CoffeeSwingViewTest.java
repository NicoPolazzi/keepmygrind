package io.github.nicopolazzi.keepmygrind.view.swing;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

public class CoffeeSwingViewTest extends AssertJSwingJUnitTestCase {
    private FrameFixture window;

    @Override
    protected void onSetUp() throws Exception {
        CoffeeSwingView coffeeView = GuiActionRunner.execute(CoffeeSwingView::new);
        JFrame frame = GuiActionRunner.execute(() -> {
            JFrame f = new JFrame();
            f.setContentPane(coffeeView);
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
}
