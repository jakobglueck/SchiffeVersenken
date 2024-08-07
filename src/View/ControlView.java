package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlView extends JPanel {
    public ControlView() {
        setLayout(new FlowLayout());

        JButton mainMenuButton = new JButton("Zurück zum Hauptmenü");
        JButton pauseGameButton = new JButton("Spiel neu starten");
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


        add(mainMenuButton);
        add(pauseGameButton);
        add(endGameButton);

        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
}
