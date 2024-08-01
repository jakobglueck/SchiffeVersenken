package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeScreenView extends JPanel {

    public HomeScreenView() {
        // Hauptpanel mit GridLayout (2 Reihen, 1 Spalte)
        setLayout(new GridLayout(2, 1));

        // Titel Label erstellen und hinzufügen
        JLabel titleLabel = new JLabel("Schiffe versenken", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24)); // Schriftart und Größe einstellen
        add(titleLabel);

        // Panel für die Buttons erstellen
        JPanel buttonPanel = new JPanel(); // Standard-Layout ist FlowLayout, zentriert die Buttons automatisch

        // Buttons erstellen
        JButton localGameButton = new JButton("Lokales Spiel starten");
        JButton debugModeButton = new JButton("Debug Modus starten");
        JButton exitButton = new JButton("Spiel beenden");

        // Action Listener für die Buttons hinzufügen
        localGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lokales Spiel starten Aktion
                JOptionPane.showMessageDialog(null, "Lokales Spiel wird gestartet...");
            }
        });

        debugModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Debug Modus starten Aktion
                JOptionPane.showMessageDialog(null, "Debug Modus wird gestartet...");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Spiel beenden Aktion
                System.exit(0);
            }
        });

        // Buttons zum Button Panel hinzufügen
        buttonPanel.add(localGameButton);
        buttonPanel.add(debugModeButton);
        buttonPanel.add(exitButton);

        // Button Panel zum Hauptpanel hinzufügen
        add(buttonPanel);
    }
}