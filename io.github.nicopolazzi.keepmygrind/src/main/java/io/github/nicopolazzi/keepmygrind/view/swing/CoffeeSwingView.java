package io.github.nicopolazzi.keepmygrind.view.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CoffeeSwingView extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField txtId;

    public CoffeeSwingView() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);

        JLabel lblId = new JLabel("id");
        GridBagConstraints gbc_lblId = new GridBagConstraints();
        gbc_lblId.anchor = GridBagConstraints.EAST;
        gbc_lblId.insets = new Insets(0, 0, 0, 5);
        gbc_lblId.gridx = 0;
        gbc_lblId.gridy = 0;
        add(lblId, gbc_lblId);

        txtId = new JTextField();
        txtId.setName("idTextBox");
        GridBagConstraints gbc_txtId = new GridBagConstraints();
        gbc_txtId.gridwidth = 2;
        gbc_txtId.insets = new Insets(0, 0, 0, 5);
        gbc_txtId.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtId.gridx = 1;
        gbc_txtId.gridy = 0;
        add(txtId, gbc_txtId);
        txtId.setColumns(10);
    }

}
