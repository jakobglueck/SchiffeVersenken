package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeScreenView extends JPanel {

    public HomeScreenView() {
        // Hauptpanel mit BorderLayout
        setLayout(new BorderLayout());

        // Inneres Panel mit GridBagLayout für zentrierte Buttons
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0); // Abstand zwischen den Buttons

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

        // Buttons zum Center Panel hinzufügen
        centerPanel.add(localGameButton, gbc);
        centerPanel.add(debugModeButton, gbc);
        centerPanel.add(exitButton, gbc);

        // Center Panel zum Hauptpanel hinzufügen
        add(centerPanel, BorderLayout.CENTER);
    }
}
