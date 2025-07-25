package io.github.nicopolazzi.keepmygrind.view.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import io.github.nicopolazzi.keepmygrind.controller.GrindProfileController;
import io.github.nicopolazzi.keepmygrind.model.Coffee;
import io.github.nicopolazzi.keepmygrind.model.GrindProfile;
import io.github.nicopolazzi.keepmygrind.view.GrindProfileView;

public class GrindProfileSwingView extends JPanel implements GrindProfileView {

    private static final long serialVersionUID = 1L;

    private JTextField txtId;
    private JComboBox<Coffee> comboBoxCoffees;
    private JTextField txtBrew;
    private JTextField txtGrams;
    private JTextField txtWater;
    private JTextField txtClicks;
    private JButton btnAdd;
    private JList<GrindProfile> listGrindProfiles;
    private JScrollPane scrollPane;
    private JButton btnDelete;
    private JLabel lblErrorMessageProfile;

    private DefaultListModel<GrindProfile> listGrindProfileModel;
    private DefaultComboBoxModel<Coffee> comboBoxCoffeeModel;

    private transient GrindProfileController grindProfileController;

    public DefaultListModel<GrindProfile> getListGrindProfileModel() {
        return listGrindProfileModel;
    }

    public DefaultComboBoxModel<Coffee> getComboBoxCoffeeModel() {
        return comboBoxCoffeeModel;
    }

    public void setGrindProfileController(GrindProfileController grindProfileController) {
        this.grindProfileController = grindProfileController;
    }

    public GrindProfileSwingView() {
        setPreferredSize(new Dimension(709, 503));
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 79, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);

        JLabel lblId = new JLabel("id");
        GridBagConstraints gbc_lblId = new GridBagConstraints();
        gbc_lblId.insets = new Insets(0, 0, 5, 5);
        gbc_lblId.anchor = GridBagConstraints.EAST;
        gbc_lblId.gridx = 0;
        gbc_lblId.gridy = 0;
        add(lblId, gbc_lblId);

        txtId = new JTextField();
        KeyAdapter btnAddEnabler = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateAddButtonEnabled();
            }
        };

        txtId.addKeyListener(btnAddEnabler);
        txtId.setName("idTextBox");
        GridBagConstraints gbc_textField_5 = new GridBagConstraints();
        gbc_textField_5.insets = new Insets(0, 0, 5, 0);
        gbc_textField_5.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_5.gridx = 1;
        gbc_textField_5.gridy = 0;
        add(txtId, gbc_textField_5);
        txtId.setColumns(10);

        JLabel lblCoffee = new JLabel("coffee");
        GridBagConstraints gbc_lblCoffee = new GridBagConstraints();
        gbc_lblCoffee.anchor = GridBagConstraints.EAST;
        gbc_lblCoffee.insets = new Insets(0, 0, 5, 5);
        gbc_lblCoffee.gridx = 0;
        gbc_lblCoffee.gridy = 1;
        add(lblCoffee, gbc_lblCoffee);

        comboBoxCoffeeModel = new DefaultComboBoxModel<>();
        comboBoxCoffees = new JComboBox<>(comboBoxCoffeeModel);
        comboBoxCoffees.addKeyListener(btnAddEnabler);
        comboBoxCoffees.addActionListener(e -> updateAddButtonEnabled());
        comboBoxCoffees.setName("coffeeComboBox");
        GridBagConstraints gbc_comboBox = new GridBagConstraints();
        gbc_comboBox.insets = new Insets(0, 0, 5, 0);
        gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboBox.gridx = 1;
        gbc_comboBox.gridy = 1;
        add(comboBoxCoffees, gbc_comboBox);

        JLabel lblBrew = new JLabel("brew");
        GridBagConstraints gbc_lblBrew = new GridBagConstraints();
        gbc_lblBrew.anchor = GridBagConstraints.EAST;
        gbc_lblBrew.insets = new Insets(0, 0, 5, 5);
        gbc_lblBrew.gridx = 0;
        gbc_lblBrew.gridy = 2;
        add(lblBrew, gbc_lblBrew);

        txtBrew = new JTextField();
        txtBrew.addKeyListener(btnAddEnabler);
        txtBrew.setName("brewTextBox");
        GridBagConstraints gbc_textField_2 = new GridBagConstraints();
        gbc_textField_2.insets = new Insets(0, 0, 5, 0);
        gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_2.gridx = 1;
        gbc_textField_2.gridy = 2;
        add(txtBrew, gbc_textField_2);
        txtBrew.setColumns(10);

        JLabel lblGrams = new JLabel("grams");
        GridBagConstraints gbc_lblGrams = new GridBagConstraints();
        gbc_lblGrams.anchor = GridBagConstraints.EAST;
        gbc_lblGrams.insets = new Insets(0, 0, 5, 5);
        gbc_lblGrams.gridx = 0;
        gbc_lblGrams.gridy = 3;
        add(lblGrams, gbc_lblGrams);

        txtGrams = new JTextField();
        txtGrams.addKeyListener(btnAddEnabler);
        txtGrams.setName("gramsTextBox");
        GridBagConstraints gbc_textField_3 = new GridBagConstraints();
        gbc_textField_3.insets = new Insets(0, 0, 5, 0);
        gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_3.gridx = 1;
        gbc_textField_3.gridy = 3;
        add(txtGrams, gbc_textField_3);
        txtGrams.setColumns(10);

        JLabel lblWater = new JLabel("water");
        GridBagConstraints gbc_lblWater = new GridBagConstraints();
        gbc_lblWater.anchor = GridBagConstraints.EAST;
        gbc_lblWater.insets = new Insets(0, 0, 5, 5);
        gbc_lblWater.gridx = 0;
        gbc_lblWater.gridy = 4;
        add(lblWater, gbc_lblWater);

        txtWater = new JTextField();
        txtWater.addKeyListener(btnAddEnabler);
        txtWater.setName("waterTextBox");
        GridBagConstraints gbc_textField_4 = new GridBagConstraints();
        gbc_textField_4.insets = new Insets(0, 0, 5, 0);
        gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_4.gridx = 1;
        gbc_textField_4.gridy = 4;
        add(txtWater, gbc_textField_4);
        txtWater.setColumns(10);

        JLabel lblClicks = new JLabel("clicks");
        GridBagConstraints gbc_lblClicks = new GridBagConstraints();
        gbc_lblClicks.anchor = GridBagConstraints.EAST;
        gbc_lblClicks.insets = new Insets(0, 0, 5, 5);
        gbc_lblClicks.gridx = 0;
        gbc_lblClicks.gridy = 5;
        add(lblClicks, gbc_lblClicks);

        txtClicks = new JTextField();
        txtClicks.addKeyListener(btnAddEnabler);
        txtClicks.setName("clicksTextBox");
        GridBagConstraints gbc_textField_1 = new GridBagConstraints();
        gbc_textField_1.insets = new Insets(0, 0, 5, 0);
        gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_1.gridx = 1;
        gbc_textField_1.gridy = 5;
        add(txtClicks, gbc_textField_1);
        txtClicks.setColumns(10);

        btnAdd = new JButton("Add");
        btnAdd.setEnabled(false);
        btnAdd.addActionListener(e -> grindProfileController.newGrindProfile(new GrindProfile(txtId.getText(),
                (Coffee) comboBoxCoffees.getSelectedItem(), txtBrew.getText(), Double.parseDouble(txtGrams.getText()),
                Double.parseDouble(txtWater.getText()), Integer.parseInt(txtClicks.getText()))));
        GridBagConstraints gbc_btnAdd = new GridBagConstraints();
        gbc_btnAdd.insets = new Insets(0, 0, 5, 0);
        gbc_btnAdd.gridwidth = 2;
        gbc_btnAdd.gridx = 0;
        gbc_btnAdd.gridy = 6;
        add(btnAdd, gbc_btnAdd);

        scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridwidth = 2;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 7;
        add(scrollPane, gbc_scrollPane);

        listGrindProfileModel = new DefaultListModel<>();
        listGrindProfiles = new JList<>(listGrindProfileModel);
        scrollPane.setViewportView(listGrindProfiles);
        listGrindProfiles
                .addListSelectionListener(e -> btnDelete.setEnabled(listGrindProfiles.getSelectedIndex() != -1));
        listGrindProfiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listGrindProfiles.setName("grindProfileList");

        btnDelete = new JButton("Delete Selected Profile");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(
                e -> grindProfileController.deleteGrindProfile(listGrindProfiles.getSelectedValue()));
        GridBagConstraints gbc_btnDeleteSelected = new GridBagConstraints();
        gbc_btnDeleteSelected.insets = new Insets(0, 0, 5, 0);
        gbc_btnDeleteSelected.gridwidth = 2;
        gbc_btnDeleteSelected.gridx = 0;
        gbc_btnDeleteSelected.gridy = 8;
        add(btnDelete, gbc_btnDeleteSelected);

        lblErrorMessageProfile = new JLabel(" ");
        lblErrorMessageProfile.setForeground(Color.RED);
        lblErrorMessageProfile.setName("errorMessageLabel");
        GridBagConstraints gbc_lblErrorMessage = new GridBagConstraints();
        gbc_lblErrorMessage.gridwidth = 2;
        gbc_lblErrorMessage.gridx = 0;
        gbc_lblErrorMessage.gridy = 9;
        add(lblErrorMessageProfile, gbc_lblErrorMessage);
    }

    private void updateAddButtonEnabled() {
        btnAdd.setEnabled(!txtId.getText().trim().isEmpty() && comboBoxCoffees.getSelectedItem() != null
                && !txtBrew.getText().trim().isEmpty() && !txtGrams.getText().trim().isEmpty()
                && !txtWater.getText().trim().isEmpty() && !txtClicks.getText().trim().isEmpty());
    }

    @Override
    public void showAllGrindProfiles(List<GrindProfile> profiles) {
        profiles.stream().forEach(listGrindProfileModel::addElement);
    }

    @Override
    public void grindProfileAdded(GrindProfile profile) {
        listGrindProfileModel.addElement(profile);
        resetErrorMessageLabel();
    }

    @Override
    public void grindProfileRemoved(GrindProfile profile) {
        listGrindProfileModel.removeElement(profile);
        resetErrorMessageLabel();
    }

    private void resetErrorMessageLabel() {
        lblErrorMessageProfile.setText(" ");
    }

    @Override
    public void showExistingGrindProfileError(GrindProfile existingProfile) {
        lblErrorMessageProfile.setText("Already existing grind profile: " + existingProfile);
    }

    @Override
    public void showCoffeeNotFoundError(String coffeeId) {
        lblErrorMessageProfile.setText("Cannot find a grind profile for the coffee with id: " + coffeeId);
    }

    @Override
    public void showNotExistingGrindProfileError(GrindProfile profile) {
        lblErrorMessageProfile.setText("Not existing grind profile: " + profile);
    }

    @Override
    public void refreshCoffees(List<Coffee> coffees) {
        comboBoxCoffeeModel.removeAllElements();
        for (Coffee coffee : coffees) {
            comboBoxCoffeeModel.addElement(coffee);
        }
    }
}
