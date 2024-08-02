package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlView extends JPanel {
    public ControlView() {
        setLayout(new FlowLayout());

        JButton mainMenuButton = new JButton("Zurück zum Hauptmenü");
        JButton pauseGameButton = new JButton("Spiel pausieren");
        JButton endGameButton = new JButton("Spiel verlassen");

        endGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        pauseGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Spiel ist pausiert!");
            }
        });

        // Hier könnte noch eine Action für das Hauptmenü-Button hinzugefügt werden, falls notwendig.

        add(mainMenuButton);
        add(pauseGameButton);
        add(endGameButton);

        // Setze Padding für das Panel
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
}
