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
    private JLabel shipPreviewLabel;
    private JLayeredPane layeredPane;

    public static final int CELL_SIZE = 50;

    public GameView(GameModel gm) {
        this.game = gm;

        setTitle("Schiffe versenken");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        shipPreviewLabel = new JLabel();
        shipPreviewLabel.setOpaque(true);
        shipPreviewLabel.setVisible(false);
        layeredPane.add(shipPreviewLabel, JLayeredPane.DRAG_LAYER);
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

        JPanel mainPanel = new JPanel(new GridBagLayout());
        layeredPane.add(mainPanel, JLayeredPane.DEFAULT_LAYER);
        mainPanel.setBounds(0, 0, getWidth(), getHeight());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        gbc.weighty = 0.05;
        mainPanel.add(createGameModePanel(), gbc);

        gbc.weighty = 0.05;
        mainPanel.add(createPlayerPanel(), gbc);

        gbc.weighty = 0.55;
        mainPanel.add(createBoardPanel(), gbc);

        gbc.weighty = 0.3;
        mainPanel.add(createInfoStatusPanel(), gbc);

        gbc.weighty = 0.1;
        controlView = new ControlView();
        mainPanel.add(controlView, gbc);

        // Ensure the correct layering
        layeredPane.setLayer(mainPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.setLayer(playerBoardOne, JLayeredPane.DEFAULT_LAYER);
        layeredPane.setLayer(playerBoardTwo, JLayeredPane.DEFAULT_LAYER);

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
        gameModeLabel = new JLabel("", SwingConstants.CENTER);
        gameModeLabel.setFont(new Font("Arial", Font.BOLD, 16));
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
                } else if (currentPlayer == playerTwo) {
                    playerBoardOne.setGridLabelsOpaque(false);
                    playerBoardTwo.setGridLabelsOpaque(true);
                }
                break;
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

    public void showShipPreview(boolean show) {
        SwingUtilities.invokeLater(() -> {
            shipPreviewLabel.setVisible(show);
            layeredPane.revalidate();
            layeredPane.repaint();
        });
    }

    public void updateShipPreview(int x, int y, int width, int height, Color color) {
        SwingUtilities.invokeLater(() -> {
            shipPreviewLabel.setBounds(x, y, width, height);
            shipPreviewLabel.setBackground(color);
            shipPreviewLabel.repaint();
            layeredPane.revalidate();
            layeredPane.repaint();
        });
    }

    public void prepareForShipPlacement() {
        playerBoardOne.setVisible(true);
        playerBoardTwo.setVisible(true);
        playerBoardOne.setEnabled(true);
        playerBoardTwo.setEnabled(true);
        playerBoardOne.setFocusable(true);
        playerBoardTwo.setFocusable(true);
        revalidate();
        repaint();
    }
}