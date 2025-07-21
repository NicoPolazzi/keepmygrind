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
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

import io.github.nicopolazzi.keepmygrind.controller.GrindProfileController;
import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.model.GrindProfile;

@RunWith(GUITestRunner.class)
public class GrindProfileSwingViewTest extends AssertJSwingJUnitTestCase {
    private FrameFixture window;
    private GrindProfileSwingView grindProfileView;

    @Mock
    private GrindProfileController grindProfileController;

    private AutoCloseable closeable;

    @Override
    protected void onSetUp() throws Exception {
        closeable = MockitoAnnotations.openMocks(this);

        JFrame frame = GuiActionRunner.execute(() -> {
            JFrame f = new JFrame();
            grindProfileView = new GrindProfileSwingView();
            grindProfileView.setGrindProfileController(grindProfileController);
            f.setContentPane(grindProfileView);
            f.pack();
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
        window.button(JButtonMatcher.withText("Add")).requireDisabled();
        window.list("grindProfileList");
        window.button(JButtonMatcher.withText("Delete Selected Profile")).requireDisabled();
        window.label("errorMessageLabel").requireText(" ");
    }

    @Test
    public void testWhenGrindProfileInformationAreNonEmptyThenAddButtonShouldBeEnabled() {
        window.textBox("idTextBox").enterText("1");
        addCoffeeToComboBox();
        window.comboBox("coffeeComboBox").selectItem(0);
        window.textBox("brewTextBox").enterText("test");
        window.textBox("gramsTextBox").enterText("15");
        window.textBox("waterTextBox").enterText("test");
        window.textBox("clicksTextBox").enterText("30");
        window.button(JButtonMatcher.withText("Add")).requireEnabled();
    }

    @Test
    public void testAddButtonDisabledWhenIdIsEmpty() {
        window.textBox("idTextBox").enterText(" ");
        addCoffeeToComboBox();
        window.comboBox("coffeeComboBox").selectItem(0);
        window.textBox("brewTextBox").enterText("espresso");
        window.textBox("gramsTextBox").enterText("15");
        window.textBox("waterTextBox").enterText("250");
        window.textBox("clicksTextBox").enterText("30");
        window.button(JButtonMatcher.withText("Add")).requireDisabled();
    }

    @Test
    public void testAddButtonDisabledWhenNoCoffeeSelected() {
        window.textBox("idTextBox").enterText("1");
        window.textBox("brewTextBox").enterText("espresso");
        window.textBox("gramsTextBox").enterText("15");
        window.textBox("waterTextBox").enterText("250");
        window.textBox("clicksTextBox").enterText("30");
        window.button(JButtonMatcher.withText("Add")).requireDisabled();
    }

    @Test
    public void testAddButtonDisabledWhenBrewIsEmpty() {
        window.textBox("idTextBox").enterText("1");
        addCoffeeToComboBox();
        window.comboBox("coffeeComboBox").selectItem(0);
        window.textBox("brewTextBox").enterText(" ");
        window.textBox("gramsTextBox").enterText("15");
        window.textBox("waterTextBox").enterText("250");
        window.textBox("clicksTextBox").enterText("30");
        window.button(JButtonMatcher.withText("Add")).requireDisabled();
    }

    @Test
    public void testAddButtonDisabledWhenGramsIsEmpty() {
        window.textBox("idTextBox").enterText("1");
        addCoffeeToComboBox();
        window.comboBox("coffeeComboBox").selectItem(0);
        window.textBox("brewTextBox").enterText("espresso");
        window.textBox("gramsTextBox").enterText(" ");
        window.textBox("waterTextBox").enterText("250");
        window.textBox("clicksTextBox").enterText("30");
        window.button(JButtonMatcher.withText("Add")).requireDisabled();
    }

    @Test
    public void testAddButtonDisabledWhenWatersIsEmpty() {
        window.textBox("idTextBox").enterText("1");
        addCoffeeToComboBox();
        window.comboBox("coffeeComboBox").selectItem(0);
        window.textBox("brewTextBox").enterText("espresso");
        window.textBox("gramsTextBox").enterText("15");
        window.textBox("waterTextBox").enterText(" ");
        window.textBox("clicksTextBox").enterText("30");
        window.button(JButtonMatcher.withText("Add")).requireDisabled();
    }

    @Test
    public void testAddButtonDisabledWhenClicksIsEmpty() {
        window.textBox("idTextBox").enterText("1");
        addCoffeeToComboBox();
        window.comboBox("coffeeComboBox").selectItem(0);
        window.textBox("brewTextBox").enterText("espresso");
        window.textBox("gramsTextBox").enterText("15");
        window.textBox("waterTextBox").enterText("250");
        window.textBox("clicksTextBox").enterText(" ");
        window.button(JButtonMatcher.withText("Add")).requireDisabled();
    }

    @Test
    public void testDeleteButtonShouldBeEnabledOnlyWhenAGrindProfileIsSelected() {
        GuiActionRunner.execute(() -> grindProfileView.getListGrindProfileModel()
                .addElement(new GrindProfile("1", new Coffee("1", "test", "test"), "test", 14.2, 100, 30)));
        window.list("grindProfileList").selectItem(0);
        JButtonFixture deleteButton = window.button(JButtonMatcher.withText("Delete Selected Profile"));
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
        var grindProfile = new GrindProfile("1", new Coffee("1", "test", "test"), "test", 14.2, 100, 30);
        GuiActionRunner.execute(() -> grindProfileView.showExistingGrindProfileError(grindProfile));
        window.label("errorMessageLabel").requireText("Already existing grind profile: " + grindProfile);
    }

    @Test
    public void testShowNotExistingGrindProfileErrorShouldShowTheMessageInTheErrorLabel() {
        var grindProfile = new GrindProfile("1", new Coffee("1", "test", "test"), "test", 14.2, 100, 30);
        GuiActionRunner.execute(() -> grindProfileView.showNotExistingGrindProfileError(grindProfile));
        window.label("errorMessageLabel").requireText("Not existing grind profile: " + grindProfile);
    }

    @Test
    public void testShowCoffeeNotFoundErrorShouldShowTheMessageInTheErrorLabel() {
        String coffeeId = "1";
        GuiActionRunner.execute(() -> grindProfileView.showCoffeeNotFoundError(coffeeId));
        window.label("errorMessageLabel")
                .requireText("Cannot find a grind profile for the coffee with id: " + coffeeId);
    }

    @Test
    public void testGrindProfileAddedShouldAddTheGrindProfileToTheListAndResetTheErrorLabel() {
        var grindProfile = new GrindProfile("1", new Coffee("1", "test", "test"), "test", 14.2, 100, 30);
        GuiActionRunner.execute(() -> grindProfileView
                .grindProfileAdded(new GrindProfile("1", new Coffee("1", "test", "test"), "test", 14.2, 100, 30)));
        String[] listContents = window.list("grindProfileList").contents();
        assertThat(listContents).containsExactly(grindProfile.toString());
        window.label("errorMessageLabel").requireText(" ");
    }

    @Test
    public void testGrindProfileRemovedShouldRemoveTheGrindProfileFromTheListAndResetTheErrorLabel() {
        var grindProfile1 = new GrindProfile("1", new Coffee("1", "test", "test"), "test", 14.2, 100, 30);
        var grindProfile2 = new GrindProfile("2", new Coffee("1", "test", "test"), "test", 14.2, 100, 30);
        GuiActionRunner.execute(() -> {
            DefaultListModel<GrindProfile> listCoffeesModel = grindProfileView.getListGrindProfileModel();
            listCoffeesModel.addElement(grindProfile1);
            listCoffeesModel.addElement(grindProfile2);
        });

        GuiActionRunner.execute(() -> grindProfileView
                .grindProfileRemoved(new GrindProfile("1", new Coffee("1", "test", "test"), "test", 14.2, 100, 30)));

        String[] listContents = window.list("grindProfileList").contents();
        assertThat(listContents).containsExactly(grindProfile2.toString());
        window.label("errorMessageLabel").requireText(" ");
    }

    @Test
    public void testAddButtonShouldDelegateToGrindProfileControllerNewGrindProfile() {
        window.textBox("idTextBox").enterText("1");
        addCoffeeToComboBox();
        window.comboBox("coffeeComboBox").selectItem(0);
        window.textBox("brewTextBox").enterText("test");
        window.textBox("gramsTextBox").enterText("14.2");
        window.textBox("waterTextBox").enterText("100");
        window.textBox("clicksTextBox").enterText("30");
        window.button(JButtonMatcher.withText("Add")).click();
        verify(grindProfileController)
                .newGrindProfile(new GrindProfile("1", new Coffee("1", "test", "test"), "test", 14.2, 100, 30));
    }

    @Test
    public void testDeleteButtonShouldDelegateToGrindProfileControllerDeleteGrindProfile() {
        var grindProfile1 = new GrindProfile("1", new Coffee("1", "test", "test"), "test", 14.2, 100, 30);
        var grindProfile2 = new GrindProfile("2", new Coffee("1", "test", "test"), "test", 14.2, 100, 30);
        GuiActionRunner.execute(() -> {
            DefaultListModel<GrindProfile> listCoffeesModel = grindProfileView.getListGrindProfileModel();
            listCoffeesModel.addElement(grindProfile1);
            listCoffeesModel.addElement(grindProfile2);
        });

        window.list("grindProfileList").selectItem(1);
        window.button(JButtonMatcher.withText("Delete Selected Profile")).click();
        verify(grindProfileController).deleteGrindProfile(grindProfile2);
    }

    private void addCoffeeToComboBox() {
        GuiActionRunner
                .execute(() -> grindProfileView.getComboBoxCoffeeModel().addElement(new Coffee("1", "test", "test")));
    }
}
