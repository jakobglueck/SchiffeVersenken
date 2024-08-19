package view;

import javax.swing.*;
import java.awt.*;

/**
 * @class HomeScreenView
 * @brief Diese Klasse stellt das Startbildschirm-Fenster für das Spiel "Schiffe versenken" bereit.
 * Diese Klasse enthält verschiedene Buttons, um verschiedene Spielmodi zu starten oder das Spiel zu beenden.
 */
public class HomeScreenView extends JFrame {

    /**
     * @brief Button zum Starten eines lokalen Spiels.
     */
    private JButton localGameButton;

    /**
     * @brief Button zum Starten eines Spiels gegen den Computer.
     */
    private JButton computerGameButton;

    /**
     * @brief Button zum Starten des Debug-Modus.
     */
    private JButton debugModeButton;

    /**
     * @brief Button zum Beenden des Spiels.
     */
    private JButton exitButton;

    /**
     * @brief Konstruktor, der die Benutzeroberfläche des Startbildschirms initialisiert.
     */
    public HomeScreenView() {
        initializeHomeScreen();
    }

    /**
     * @brief Initialisiert den Startbildschirm mit Titel und Spieloptionen.
     *
     * Erstellt ein Hauptpanel `mainPanel`, das zwei Bereiche enthält:
     * Ein Panel für den Titel des Spiels und ein Panel mit Buttons für die verschiedenen Spieloptionen.
     */
    private void initializeHomeScreen() {
        this.setTitle("Schiffe versenken");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        this.add(mainPanel);

        JLabel titleLabel = new JLabel("Schiffe versenken", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        mainPanel.add(titleLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.localGameButton = new JButton("Normales Spiel starten");
        this.computerGameButton = new JButton("Computer Spiel starten");
        this.debugModeButton = new JButton("Debug Modus starten");
        this.exitButton = new JButton("Spiel beenden");

        buttonPanel.add(this.localGameButton);
        buttonPanel.add(this.computerGameButton);
        buttonPanel.add(this.debugModeButton);
        buttonPanel.add(this.exitButton);

        mainPanel.add(buttonPanel);

        this.setVisible(true);
    }


    /**
     * @brief Gibt den Button zum Starten eines lokalen Spiels zurück.
     *
     * @return den Button zum Starten eines lokalen Spiels.
     */
    public JButton getLocalGameButton() {
        return this.localGameButton;
    }

    /**
     * @brief Gibt den Button zum Starten eines Spiels gegen den Computer zurück.
     *
     * @return den Button zum Starten eines Spiels gegen den Computer.
     */
    public JButton getComputerGameButton() {
        return this.computerGameButton;
    }

    /**
     * @brief Gibt den Button zum Starten des Debug-Modus zurück.
     *
     * @return den Button zum Starten des Debug-Modus.
     */
    public JButton getDebugModeButton() {
        return this.debugModeButton;
    }

    /**
     * @brief Gibt den Button zum Beenden des Spiels zurück.
     *
     * @return den Button zum Beenden des Spiels.
     */
    public JButton getExitButton() {
        return this.exitButton;
    }
}
