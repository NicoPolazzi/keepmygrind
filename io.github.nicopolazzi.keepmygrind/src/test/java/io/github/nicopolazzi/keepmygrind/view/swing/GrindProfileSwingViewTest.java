package io.github.nicopolazzi.keepmygrind.view.swing;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.model.GrindProfile;

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

    @Test
    public void testDeleteButtonShouldBeEnabledOnlyWhenAGrindProfileIsSelected() {
        GuiActionRunner.execute(() -> grindProfileView.getListGrindProfileModel()
                .addElement(new GrindProfile("1", new Coffee("1", "test", "test"), "test", 14.2, 100, 30)));
        window.list("grindProfileList").selectItem(0);
        JButtonFixture deleteButton = window.button(JButtonMatcher.withText("Delete Selected"));
        deleteButton.requireEnabled();
        window.list("grindProfileList").clearSelection();
        deleteButton.requireDisabled();
    }

    @Test
    public void testsShowAllGrindProfilesShouldAddGrindProfileInformationToTheList() {
        var grindProfile1 = new GrindProfile("1", new Coffee("1", "test", "test"), "test", 14.2, 100, 30);
        var grindProfile2 = new GrindProfile("2", new Coffee("1", "test", "test"), "test", 14.2, 100, 30);
        GuiActionRunner.execute(() -> grindProfileView.showAllGrindProfiles(asList(grindProfile1, grindProfile2)));
        String[] listContents = window.list("grindProfileList").contents();
        assertThat(listContents).containsExactly(grindProfile1.toString(), grindProfile2.toString());
    }

    @Test
    public void testShowExistingGrindProfileErrorShouldShowTheMessageInTheErrorLabel() {
        var grindProfile1 = new GrindProfile("1", new Coffee("1", "test", "test"), "test", 14.2, 100, 30);
        GuiActionRunner.execute(() -> grindProfileView.showExistingGrindProfileError(grindProfile1));
        window.label("errorMessageLabel").requireText("Already existing grind profile: " + grindProfile1);
    }

    @Test
    public void testShowNotExistingGrindProfileErrorShouldShowTheMessageInTheErrorLabel() {
        var grindProfile1 = new GrindProfile("1", new Coffee("1", "test", "test"), "test", 14.2, 100, 30);
        GuiActionRunner.execute(() -> grindProfileView.showNotExistingGrindProfileError(grindProfile1));
        window.label("errorMessageLabel").requireText("Not existing grind profile: " + grindProfile1);
    }

}
