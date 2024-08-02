package View;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameView extends JFrame {

    private PlayerModel playerOne;
    private PlayerModel playerTwo;
    private BoardView playerBoardOne;
    private BoardView playerBoardTwo;
    private BoardModel boardModel;

    public GameView(GameModel gm) {
        this.gameModel = gm;

        setTitle("Schiffe versenken");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);


        this.playerOne = gm.getPlayerOne();
        this.playerTwo = gm.getPlayerTwo();


        this.playerBoardOne = new BoardView(playerOne.getBoard());
        this.playerBoardTwo = new BoardView(playerTwo.getBoard());

        // bm.randomlyPlaceShips(); // Zufällige Platzierung der Schiffe auf dem ersten Spielbrett
        playerBoardOne.updateBoard(); // Aktualisiere das Spielbrett

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Labels für Spieler 1 und Spieler 2
        addComponent(new JLabel(this.playerOne.getPlayerName(), JLabel.CENTER), gbc, 0, 0, 1, 1, 10, 0.0, 0.0, GridBagConstraints.NONE);
        addComponent(new JLabel(this.playerTwo.getPlayerName(), JLabel.CENTER), gbc, 2, 0, 1, 1, 10, 0.0, 0.0, GridBagConstraints.NONE);

        // BoardView für Spieler 1 und Spieler 2
        addComponent(playerBoardOne, gbc, 0, 1, 1, 1, 10, 0.5, 0.5, GridBagConstraints.BOTH);
        addComponent(new JPanel(), gbc, 1, 1, 1, 1, 10, 0.0, 0.0, GridBagConstraints.BOTH);
        addComponent(playerBoardTwo, gbc, 2, 1, 1, 1, 10, 0.5, 0.5, GridBagConstraints.BOTH);

        // Unterer Bereich für Bedienelemente
        JPanel controlPanel = new JPanel(new FlowLayout());
        addControlButtons(controlPanel);
        addComponent(controlPanel, gbc, 0, 2, 3, 1, 10, 1.0, 0.0, GridBagConstraints.HORIZONTAL);

        setVisible(true);
    }

    private void addControlButtons(JPanel controlPanel) {
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
                // Implementieren Sie hier die Aktion zum Pausieren des Spiels
                JOptionPane.showMessageDialog(null, "Spiel ist pausiert!");
            }
        });

        /*
        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewGame();
            }
        });
        */

        controlPanel.add(mainMenuButton);
        controlPanel.add(pauseGameButton);
        controlPanel.add(endGameButton);

    }

    private void addComponent(Component component, GridBagConstraints gbc, int gridx, int gridy, int gridwidth, int gridheight, int insets, double weightx, double weighty, int fill) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.insets = new Insets(insets, insets, insets, insets);
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.fill = fill;
        add(component, gbc);
    }
}
