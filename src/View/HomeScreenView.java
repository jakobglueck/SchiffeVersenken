package View;

import javax.swing.*;
import java.awt.*;

public class HomeScreenView extends JFrame {
    private JButton localGameButton;
    private JButton computerGameButton;
    private JButton debugModeButton;
    private JButton exitButton;

    public HomeScreenView() {
        setTitle("Schiffe versenken");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(2, 1));

        JLabel titleLabel = new JLabel("Schiffe versenken", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(titleLabel);

        JPanel buttonPanel = new JPanel();

        localGameButton = new JButton("Lokales Spiel starten");
        computerGameButton = new JButton("Computer Spiel starten");
        debugModeButton = new JButton("Debug Modus starten");
        exitButton = new JButton("Spiel beenden");

        buttonPanel.add(localGameButton);
        buttonPanel.add(computerGameButton);
        buttonPanel.add(debugModeButton);
        buttonPanel.add(exitButton);

        add(buttonPanel);
    }

    public JButton getLocalGameButton() {
        return localGameButton;
    }

    public JButton getComputerGameButton() {
        return computerGameButton;
    }

    public JButton getDebugModeButton() {
        return debugModeButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}