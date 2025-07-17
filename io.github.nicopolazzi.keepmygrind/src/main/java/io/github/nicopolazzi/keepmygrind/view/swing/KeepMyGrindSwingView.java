package io.github.nicopolazzi.keepmygrind.view.swing;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class KeepMyGrindSwingView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLayeredPane layeredPane;

    public KeepMyGrindSwingView() {
        setTitle("KeepMyGrind");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 758, 551);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[] { 317, 0, 0 };
        gbl_contentPane.rowHeights = new int[] { 0, 0, 0 };
        gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gbl_contentPane.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        contentPane.setLayout(gbl_contentPane);

        JButton btnCoffee = new JButton("Coffee");
        btnCoffee.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (layeredPane.getLayout());
                cl.show(layeredPane, "coffee");
            }
        });
        GridBagConstraints gbc_btnCoffee = new GridBagConstraints();
        gbc_btnCoffee.insets = new Insets(0, 0, 5, 5);
        gbc_btnCoffee.gridx = 0;
        gbc_btnCoffee.gridy = 0;
        contentPane.add(btnCoffee, gbc_btnCoffee);

        JButton btnGrindProfile = new JButton("Grind Profile");
        btnGrindProfile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (layeredPane.getLayout());
                cl.show(layeredPane, "grindProfile");
            }
        });
        GridBagConstraints gbc_btnGrindProfile = new GridBagConstraints();
        gbc_btnGrindProfile.insets = new Insets(0, 0, 5, 0);
        gbc_btnGrindProfile.gridx = 1;
        gbc_btnGrindProfile.gridy = 0;
        contentPane.add(btnGrindProfile, gbc_btnGrindProfile);

        layeredPane = new JLayeredPane();
        GridBagConstraints gbc_layeredPane = new GridBagConstraints();
        gbc_layeredPane.gridwidth = 2;
        gbc_layeredPane.fill = GridBagConstraints.BOTH;
        gbc_layeredPane.gridx = 0;
        gbc_layeredPane.gridy = 1;
        contentPane.add(layeredPane, gbc_layeredPane);
        layeredPane.setLayout(new CardLayout(0, 0));

        JPanel blankPanel = new JPanel();
        layeredPane.add(blankPanel, "blank");

        JPanel coffeePanel = new CoffeeSwingView();
        layeredPane.add(coffeePanel, "coffee");

        JPanel grindProfilePanel = new GrindProfileSwingView();
        layeredPane.add(grindProfilePanel, "grindProfile");

    }

}
