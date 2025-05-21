package io.github.nicopolazzi.keepmygrind.view.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class CoffeeSwingView extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField txtId;
    private JTextField txtOrigin;
    private JTextField txtProcess;

    public CoffeeSwingView() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 46, 645, 0 };
        gridBagLayout.rowHeights = new int[] { 21, 21, 21, 27, 354, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);

        JLabel lblId = new JLabel("id");
        GridBagConstraints gbc_lblId = new GridBagConstraints();
        gbc_lblId.anchor = GridBagConstraints.EAST;
        gbc_lblId.insets = new Insets(0, 0, 5, 5);
        gbc_lblId.gridx = 0;
        gbc_lblId.gridy = 0;
        add(lblId, gbc_lblId);

        txtId = new JTextField();
        txtId.setName("idTextBox");
        GridBagConstraints gbc_txtId = new GridBagConstraints();
        gbc_txtId.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtId.insets = new Insets(0, 0, 5, 0);
        gbc_txtId.gridx = 1;
        gbc_txtId.gridy = 0;
        add(txtId, gbc_txtId);
        txtId.setColumns(10);

        JLabel lblOrigin = new JLabel("origin");
        GridBagConstraints gbc_lblOrigin = new GridBagConstraints();
        gbc_lblOrigin.insets = new Insets(0, 0, 5, 5);
        gbc_lblOrigin.gridx = 0;
        gbc_lblOrigin.gridy = 1;
        add(lblOrigin, gbc_lblOrigin);

        txtOrigin = new JTextField();
        txtOrigin.setName("originTextBox");
        GridBagConstraints gbc_txtOrigin = new GridBagConstraints();
        gbc_txtOrigin.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtOrigin.insets = new Insets(0, 0, 5, 0);
        gbc_txtOrigin.gridx = 1;
        gbc_txtOrigin.gridy = 1;
        add(txtOrigin, gbc_txtOrigin);
        txtOrigin.setColumns(10);

        JLabel lblProcess = new JLabel("process");
        GridBagConstraints gbc_lblProcess = new GridBagConstraints();
        gbc_lblProcess.insets = new Insets(0, 0, 5, 5);
        gbc_lblProcess.gridx = 0;
        gbc_lblProcess.gridy = 2;
        add(lblProcess, gbc_lblProcess);

        txtProcess = new JTextField();
        txtProcess.setName("processTextBox");
        GridBagConstraints gbc_txtProcess = new GridBagConstraints();
        gbc_txtProcess.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtProcess.insets = new Insets(0, 0, 5, 0);
        gbc_txtProcess.gridx = 1;
        gbc_txtProcess.gridy = 2;
        add(txtProcess, gbc_txtProcess);
        txtProcess.setColumns(10);

        JButton btnAdd = new JButton("Add");
        btnAdd.setEnabled(false);
        GridBagConstraints gbc_btnAdd = new GridBagConstraints();
        gbc_btnAdd.gridwidth = 2;
        gbc_btnAdd.insets = new Insets(0, 0, 5, 0);
        gbc_btnAdd.gridx = 0;
        gbc_btnAdd.gridy = 3;
        add(btnAdd, gbc_btnAdd);

        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.gridwidth = 2;
        gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 4;
        add(scrollPane, gbc_scrollPane);

        JList list = new JList();
        scrollPane.setViewportView(list);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setName("coffeeList");

        JButton btnDeleteSelected = new JButton("Delete Selected");
        btnDeleteSelected.setEnabled(false);
        GridBagConstraints gbc_btnDeleteSelected = new GridBagConstraints();
        gbc_btnDeleteSelected.insets = new Insets(0, 0, 5, 0);
        gbc_btnDeleteSelected.gridwidth = 2;
        gbc_btnDeleteSelected.gridx = 0;
        gbc_btnDeleteSelected.gridy = 5;
        add(btnDeleteSelected, gbc_btnDeleteSelected);

        JLabel lblNewLabel = new JLabel(" ");
        lblNewLabel.setName("errorMessageLabel");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.gridwidth = 2;
        gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 6;
        add(lblNewLabel, gbc_lblNewLabel);

        KeyAdapter btnAddEnabler = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                btnAdd.setEnabled(!txtId.getText().isEmpty() && !txtOrigin.getText().isEmpty()
                        && !txtProcess.getText().isEmpty());
            }
        };

        txtId.addKeyListener(btnAddEnabler);
        txtOrigin.addKeyListener(btnAddEnabler);
        txtProcess.addKeyListener(btnAddEnabler);

    }

}
