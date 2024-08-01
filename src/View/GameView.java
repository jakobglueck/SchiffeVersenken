package View;

import model.BoardModel;
import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {

    private BoardView playerBoardOne;
    private BoardView playerBoardTwo;

    public GameView(BoardModel bm) {
        setTitle("Schiffe versenken");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        this.playerBoardOne = new BoardView(bm);
        this.playerBoardTwo = new BoardView(bm);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Label für Spieler 1
        JLabel playerOneLabel = new JLabel("Player 1", JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(playerOneLabel, gbc);

        // Label für Spieler 2
        JLabel playerTwoLabel = new JLabel("Player 2", JLabel.CENTER);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        add(playerTwoLabel, gbc);

        // BoardView für Spieler 1
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(playerBoardOne, gbc);

        // Leeres Panel als Platzhalter
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        add(new JPanel(), gbc);

        // BoardView für Spieler 2
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        add(playerBoardTwo, gbc);

        // Unterer Bereich mit Bedienelementen
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(controlPanel, gbc);

        setVisible(true);
    }
}
