package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeScreenView extends JPanel {

    public HomeScreenView() {
        setLayout(new GridLayout(2, 1));

        JLabel titleLabel = new JLabel("Schiffe versenken", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24)); // Schriftart und Größe einstellen
        add(titleLabel);

        JPanel buttonPanel = new JPanel(); // Standard-Layout ist FlowLayout, zentriert die Buttons automatisch

        JButton localGameButton = new JButton("Lokales Spiel starten");
        JButton debugModeButton = new JButton("Debug Modus starten");
        JButton exitButton = new JButton("Spiel beenden");

        localGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Lokales Spiel wird gestartet...");
            }
        });

        debugModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Debug Modus wird gestartet...");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(localGameButton);
        buttonPanel.add(debugModeButton);
        buttonPanel.add(exitButton);

        add(buttonPanel);
    }
}