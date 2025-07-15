package io.github.nicopolazzi.keepmygrind.view.swing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import io.github.nicopolazzi.keepmygrind.model.GrindProfile;
import io.github.nicopolazzi.keepmygrind.view.GrindProfileView;

public class GrindProfileSwingView extends JPanel implements GrindProfileView {

    private static final long serialVersionUID = 1L;

    private JTextField txtId;
    private JTextField txtCoffee;
    private JTextField txtBrew;
    private JTextField txtGrams;
    private JTextField txtWater;
    private JTextField txtClicks;

    private DefaultListModel<GrindProfile> listGrindProfileModel;
    private JList<GrindProfile> listGrindProfiles;

    private JLabel lblErrorMessage;

    public GrindProfileSwingView() {
        setPreferredSize(new Dimension(700, 500));
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

        txtCoffee = new JTextField();
        txtCoffee.setName("coffeeTextBox");
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.insets = new Insets(0, 0, 5, 0);
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.gridx = 1;
        gbc_textField.gridy = 1;
        add(txtCoffee, gbc_textField);
        txtCoffee.setColumns(10);

        JLabel lblBrew = new JLabel("brew");
        GridBagConstraints gbc_lblBrew = new GridBagConstraints();
        gbc_lblBrew.anchor = GridBagConstraints.EAST;
        gbc_lblBrew.insets = new Insets(0, 0, 5, 5);
        gbc_lblBrew.gridx = 0;
        gbc_lblBrew.gridy = 2;
        add(lblBrew, gbc_lblBrew);

        txtBrew = new JTextField();
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
        txtClicks.setName("clicksTextBox");
        GridBagConstraints gbc_textField_1 = new GridBagConstraints();
        gbc_textField_1.insets = new Insets(0, 0, 5, 0);
        gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_1.gridx = 1;
        gbc_textField_1.gridy = 5;
        add(txtClicks, gbc_textField_1);
        txtClicks.setColumns(10);

        JButton btnAdd = new JButton("Add");
        btnAdd.setEnabled(false);
        GridBagConstraints gbc_btnAdd = new GridBagConstraints();
        gbc_btnAdd.insets = new Insets(0, 0, 5, 0);
        gbc_btnAdd.gridwidth = 2;
        gbc_btnAdd.gridx = 0;
        gbc_btnAdd.gridy = 6;
        add(btnAdd, gbc_btnAdd);

        JScrollPane scrollPane = new JScrollPane();
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
        listGrindProfiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listGrindProfiles.setName("grindProfileList");

        JButton btnDeleteSelected = new JButton("Delete Selected");
        btnDeleteSelected.setEnabled(false);
        GridBagConstraints gbc_btnDeleteSelected = new GridBagConstraints();
        gbc_btnDeleteSelected.insets = new Insets(0, 0, 5, 0);
        gbc_btnDeleteSelected.gridwidth = 2;
        gbc_btnDeleteSelected.gridx = 0;
        gbc_btnDeleteSelected.gridy = 8;
        add(btnDeleteSelected, gbc_btnDeleteSelected);

        lblErrorMessage = new JLabel(" ");
        lblErrorMessage.setName("errorMessageLabel");
        GridBagConstraints gbc_lblErrorMessage = new GridBagConstraints();
        gbc_lblErrorMessage.gridwidth = 2;
        gbc_lblErrorMessage.insets = new Insets(0, 0, 0, 5);
        gbc_lblErrorMessage.gridx = 0;
        gbc_lblErrorMessage.gridy = 9;
        add(lblErrorMessage, gbc_lblErrorMessage);

        KeyAdapter btnAddEnabler = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                btnAdd.setEnabled(!txtId.getText().isEmpty() && !txtCoffee.getText().isEmpty()
                        && !txtBrew.getText().isEmpty() && !txtGrams.getText().isEmpty()
                        && !txtWater.getText().isEmpty() && !txtClicks.getText().isEmpty());
            }
        };

        txtId.addKeyListener(btnAddEnabler);
        txtCoffee.addKeyListener(btnAddEnabler);
        txtBrew.addKeyListener(btnAddEnabler);
        txtGrams.addKeyListener(btnAddEnabler);
        txtWater.addKeyListener(btnAddEnabler);
        txtClicks.addKeyListener(btnAddEnabler);

        listGrindProfiles.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                btnDeleteSelected.setEnabled(listGrindProfiles.getSelectedIndex() != -1);
            }
        });
    }

    @Override
    public void showAllGrindProfiles(List<GrindProfile> profiles) {
        profiles.stream().forEach(listGrindProfileModel::addElement);
    }

    @Override
    public void grindProfileAdded(GrindProfile profile) {
        // TODO Auto-generated method stub

    }

    @Override
    public void grindProfileRemoved(GrindProfile profile) {
        // TODO Auto-generated method stub

    }

    @Override
    public void showExistingGrindProfileError(GrindProfile existingProfile) {
        lblErrorMessage.setText("Already existing grind profile: " + existingProfile);
    }

    @Override
    public void showCoffeeNotFoundError(String coffeeId) {
        lblErrorMessage.setText("Cannot find a grind profile for the coffee with id: " + coffeeId);
    }

    @Override
    public void showNotExistingGrindProfileError(GrindProfile profile) {
        lblErrorMessage.setText("Not existing grind profile: " + profile);
    }

    public DefaultListModel<GrindProfile> getListGrindProfileModel() {
        return listGrindProfileModel;
    }

}
