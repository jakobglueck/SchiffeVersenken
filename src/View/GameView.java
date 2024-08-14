package View;

import model.*;
import utils.GameState;

import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {

    private PlayerModel playerOne;
    private PlayerModel playerTwo;
    private GameModel game;
    private BoardView playerBoardOne;
    private BoardView playerBoardTwo;
    private InfoPanelView infoPanelViewOne;
    private InfoPanelView infoPanelViewTwo;
    private StatusView statusView;
    private ControlView controlView;

    private JLabel gameModeLabel;

    public GameView(GameModel gm) {
        this.game = gm;

        setTitle("Schiffe versenken");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setResizable(false);
    }

    public void createPlayerBase() {
        this.playerOne = game.getPlayerOne();
        this.playerTwo = game.getPlayerTwo();

        if (playerOne == null || playerTwo == null) {
            throw new IllegalStateException("Spieler müssen vor dem Aufruf dieser Methode initialisiert werden.");
        }

        this.playerBoardOne = new BoardView(playerOne.getBoard());
        this.playerBoardTwo = new BoardView(playerTwo.getBoard());

        playerBoardOne.updateBoard();
        playerBoardTwo.updateBoard();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        gbc.weighty = 0.1;
        add(createGameModePanel(), gbc);

        // Zeile 1: PlayerPanel (10% Höhe)
        gbc.weighty = 0.1;
        add(createPlayerPanel(), gbc);

        // Zeile 2: BoardPanel (60% Höhe)
        gbc.weighty = 0.6;
        add(createBoardPanel(), gbc);

        // Zeile 3: InfoPanel/StatusPanel (20% Höhe)
        gbc.weighty = 0.2;
        add(createInfoStatusPanel(), gbc);

        // Zeile 4: ControlPanel (10% Höhe)
        gbc.weighty = 0.1;
        controlView = new ControlView();
        add(controlView, gbc);

        setVisible(true);
    }

    private JPanel createPlayerPanel() {
        JPanel playerPanel = new JPanel(new GridLayout(1, 2));
        PlayerView playerViewOne = new PlayerView(this.playerOne.getPlayerName());
        PlayerView playerViewTwo = new PlayerView(this.playerTwo.getPlayerName());

        playerPanel.add(playerViewOne);
        playerPanel.add(playerViewTwo);

        return playerPanel;
    }

    private JPanel createBoardPanel() {
        JPanel boardPanel = new JPanel(new GridLayout(1, 2));
        boardPanel.add(playerBoardOne);
        boardPanel.add(playerBoardTwo);
        return boardPanel;
    }


    private JPanel createGameModePanel() {
        JPanel gameModePanel = new JPanel(new GridLayout(1, 1));
        gameModeLabel = new JLabel("", SwingConstants.CENTER); // Spielmodus dynamisch setzen
        gameModeLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Schriftart und Größe anpassen
        gameModePanel.add(gameModeLabel);

        return gameModePanel;
    }

    public void updateGameModePanel(String gameMode){
        this.gameModeLabel.setText(gameMode);
    }
    private JPanel createInfoStatusPanel() {
        JPanel infoStatusPanel = new JPanel(new BorderLayout());
        infoPanelViewOne = new InfoPanelView();
        statusView = new StatusView();
        infoPanelViewTwo = new InfoPanelView();

        infoStatusPanel.add(infoPanelViewOne, BorderLayout.WEST);
        infoStatusPanel.add(statusView, BorderLayout.CENTER);
        infoStatusPanel.add(infoPanelViewTwo, BorderLayout.EAST);

        return infoStatusPanel;
    }

    public BoardView getPlayerBoardOne() {
        return this.playerBoardOne;
    }

    public BoardView getPlayerBoardTwo() {
        return this.playerBoardTwo;
    }

    public void updateBoardVisibility(PlayerModel currentPlayer) {
        GameState gameState = game.getGameState();

        switch (gameState) {
            case NORMAL:
                if (currentPlayer == playerOne) {
                    playerBoardOne.setGridLabelsOpaque(true);
                    playerBoardTwo.setGridLabelsOpaque(false);
                    break;
                } else if (currentPlayer == playerTwo) {
                    playerBoardOne.setGridLabelsOpaque(false);
                    playerBoardTwo.setGridLabelsOpaque(true);
                    break;
                }
            case DEBUG:
                playerBoardOne.setGridLabelsOpaque(true);
                playerBoardTwo.setGridLabelsOpaque(true);
                break;
            case COMPUTER:
                playerBoardOne.setGridLabelsOpaque(true);
                playerBoardTwo.setGridLabelsOpaque(false);
                break;
        }
    }

    public InfoPanelView getInfoPanelViewOne() {
        return this.infoPanelViewOne;
    }

    public InfoPanelView getInfoPanelViewTwo() {
        return this.infoPanelViewTwo;
    }

    public ControlView getControlView() {
        return this.controlView;
    }

    public StatusView getStatusView() {
        return this.statusView;
    }

    public void resetView() {
        playerBoardOne.resetBoard();
        playerBoardTwo.resetBoard();
        statusView.reset();
        infoPanelViewOne.reset();
        infoPanelViewTwo.reset();
    }

    public int showGameOverDialog(String winner) {
        return JOptionPane.showOptionDialog(
                this,
                "Spiel vorbei! " + winner + " gewinnt!\nMöchtest du ein neues Spiel starten oder zum Hauptmenü zurückkehren?",
                "Spiel beendet",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Neues Spiel", "Hauptmenü"},
                "Neues Spiel"
        );
    }
}
