package View;

import model.*;
import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {

    private PlayerModel playerOne;
    private PlayerModel playerTwo;
    private BoardView playerBoardOne;
    private BoardView playerBoardTwo;
    private GameModel gameModel;
    private InfoPanelView infoPanelView;
    private StatusView statusView;
    private ControlView controlView;

    public GameView(GameModel gm) {
        this.gameModel = gm;

        setTitle("Schiffe versenken");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false); // Fenstergröße fixieren

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

        // ControlView direkt unter den Spielernamen
        controlView = new ControlView();
        add(controlView, BorderLayout.AFTER_LAST_LINE);

        // BoardView für Spieler 1 und Spieler 2
        JPanel boardPanel = new JPanel(new GridLayout(1, 2));
        boardPanel.add(playerBoardOne);
        boardPanel.add(playerBoardTwo);

        add(boardPanel, BorderLayout.CENTER);

        // Unterer Bereich für InfoPanelView und StatusView
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        infoPanelView = new InfoPanelView();
        statusView = new StatusView(); // Füge das StatusView hinzu, wenn es benötigt wird
        bottomPanel.add(infoPanelView);
        bottomPanel.add(statusView); // Füge das StatusView dem bottomPanel hinzu

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true); // Fenster sichtbar machen
    }

}
