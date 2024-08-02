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
    private GameModel gameModel;

    public GameView(GameModel gm) {
        this.gameModel = gm;

        setTitle("Schiffe versenken");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        this.playerOne = gm.getPlayerOne();
        this.playerTwo = gm.getPlayerTwo();

        this.playerBoardOne = new BoardView(playerOne.getBoard());
        this.playerBoardTwo = new BoardView(playerTwo.getBoard());

        playerBoardOne.updateBoard(); // Aktualisiere das Spielbrett

        setLayout(new BorderLayout());

        // PlayerViews für Spieler 1 und Spieler 2
        JPanel playerNamePanel = new JPanel(new GridLayout(1, 2));
        PlayerView playerViewOne = new PlayerView(this.playerOne.getPlayerName());
        PlayerView playerViewTwo = new PlayerView(this.playerTwo.getPlayerName());

        playerNamePanel.add(playerViewOne);
        playerNamePanel.add(playerViewTwo);

        add(playerNamePanel, BorderLayout.NORTH);

        // BoardView für Spieler 1 und Spieler 2
        JPanel boardPanel = new JPanel(new GridLayout(1, 2));
        boardPanel.add(playerBoardOne);
        boardPanel.add(playerBoardTwo);

        add(boardPanel, BorderLayout.CENTER);

        // Unterer Bereich für Bedienelemente
        JPanel controlPanel = new JPanel(new FlowLayout());
        addControlButtons(controlPanel);
        add(controlPanel, BorderLayout.SOUTH);

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
                JOptionPane.showMessageDialog(null, "Spiel ist pausiert!");
            }
        });

        controlPanel.add(mainMenuButton);
        controlPanel.add(pauseGameButton);
        controlPanel.add(endGameButton);
    }
}
