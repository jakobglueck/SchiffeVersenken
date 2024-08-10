package controller;

import model.ComputerPlayerModel;
import model.GameModel;
import model.PlayerModel;
import View.*;
import utils.GameState;

import javax.swing.*;
import java.awt.event.*;

public class GameController {

    private GameModel gameModel;
    private GameView gameView;
    private HomeScreenView homeScreenView;
    private JFrame homeFrame;

    public GameController(GameModel gameModel, GameView gameView, HomeScreenView homeScreenView, JFrame homeFrame) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.homeScreenView = homeScreenView;
        this.homeFrame = homeFrame;
        this.initializeListeners();
    }

    private void initializeListeners() {

        homeScreenView.getLocalGameButton().addActionListener(new LocalGameButtonListener());
        homeScreenView.getDebugModeButton().addActionListener(new DebugModeButtonListener());
        homeScreenView.getExitButton().addActionListener(new ExitButtonListener());
        gameView.getControlView().getMainMenuButton().addActionListener(new MainMenuButtonListener());
        gameView.getControlView().getPauseGameButton().addActionListener(new PauseGameButtonListener());
        gameView.getControlView().getEndGameButton().addActionListener(new EndGameButtonListener());
        gameView.getPlayerBoardOne().addMouseListener(new BoardClickListener());
        gameView.getPlayerBoardTwo().addMouseListener(new BoardClickListener());
    }

    public void showHomeScreen() {
        homeScreenView.setVisible(true);
        gameView.setVisible(false);
    }

    public void startGame(GameState gameState) {
        homeFrame.setVisible(false);
        String playerOneName = JOptionPane.showInputDialog("Bitte Namen für Spieler 1 eingeben:");
        String playerTwoName = null;

        if (gameState == GameState.NORMAL || gameState == GameState.DEBUG) {
            playerTwoName = JOptionPane.showInputDialog("Bitte Namen für Spieler 2 eingeben:");
        } else {
            playerTwoName = "Computer";
        }

        gameModel.createPlayerWithNames(playerOneName, playerTwoName);
        gameModel.setGameState(gameState);

        gameView.createPlayerBase();
        gameModel.startGame();
        gameView.setVisible(true);
        if(gameState == GameState.NORMAL || gameState == GameState.COMPUTER){
            this.handleManualShipPlacement();
        }
        else{
            this.runGameLoop();
        }
    }

    private void handleManualShipPlacement() {
        int shipTurns = gameModel.getPlayerTwo() instanceof ComputerPlayerModel ? 1 : 2;

        // Beginne mit dem ersten Spieler
        placeShipsForCurrentPlayer();

        // Wenn es einen zweiten menschlichen Spieler gibt, wechsle nach der Platzierung
        if (shipTurns == 2) {
            gameModel.switchPlayer();
            placeShipsForCurrentPlayer();
        }

        JOptionPane.showMessageDialog(gameView, "Alle Schiffe platziert. Das Spiel beginnt jetzt!");
        gameView.getStatusView().updateStatus("Spiel beginnt!");
        runGameLoop(); // Start des Spiels nach der Schiffsplatzierung
    }

    private void placeShipsForCurrentPlayer() {
        PlayerModel currentPlayer = gameModel.getCurrentPlayer();

        if (currentPlayer == null) {
            System.out.println("Current player is null");
            return;
        }

        gameView.getStatusView().updateStatus(currentPlayer.getPlayerName() + ", platziere deine Schiffe");

        // Entferne alte Listener, um Überschneidungen zu vermeiden
        for (MouseListener listener : gameView.getPlayerBoardOne().getMouseListeners()) {
            gameView.getPlayerBoardOne().removeMouseListener(listener);
        }

        gameView.getPlayerBoardOne().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BoardView clickedBoard = (BoardView) e.getSource();
                int cellSize = clickedBoard.getCellSize();
                int x = e.getX() / cellSize;
                int y = e.getY() / cellSize;
                boolean horizontal = SwingUtilities.isLeftMouseButton(e);

                if (gameModel.placeNextShip(x, y, horizontal)) {
                    updateGameView();
                    if (currentPlayer.allShipsPlaced()) {
                        gameView.getPlayerBoardOne().removeMouseListener(this);
                        if (gameModel.getCurrentPlayer() instanceof ComputerPlayerModel) {
                            gameModel.switchPlayer(); // Falls Computer, automatisch den Spieler wechseln
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(gameView, "Ungültige Schiffsplatzierung. Versuche es erneut.");
                }
            }
        });
    }

    private void handlePlayerMove() {
        gameView.getPlayerBoardOne().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BoardView clickedBoard = (BoardView) e.getSource();
                int cellSize = clickedBoard.getCellSize();
                int x = e.getX() / cellSize;
                int y = e.getY() / cellSize;

                gameModel.playerTurn(x, y);
                updateGameView();

                if (gameModel.isGameOver()) {
                    showGameOverDialog();
                }
            }
        });
    }

    private void handleShipPlacement(int startX, int startY, boolean horizontal) {
        if (gameModel.placeNextShip(startX, startY, horizontal)) {
            updateGameView();
            if (gameModel.allShipsPlaced()) {
                JOptionPane.showMessageDialog(gameView, "Alle Schiffe platziert. Bereit zum Spielstart!");
                gameView.getStatusView().updateStatus("Spiel beginnt!");
            }
        } else {
            JOptionPane.showMessageDialog(gameView, "Ungültige Schiffsplatzierung. Versuche es erneut.");
        }
    }

    private void updateGameView() {
        gameView.getPlayerBoardOne().updateBoard();
        gameView.getPlayerBoardTwo().updateBoard();
        gameView.updateBoardVisibility(gameModel.getCurrentPlayer());
        gameView.getStatusView().updateStatus("Aktueller Spieler: " + gameModel.getCurrentPlayer().getPlayerName());
        this.updateInfoPanel();
    }

    private void updateInfoPanel() {
        PlayerModel currentPlayer = gameModel.getCurrentPlayer();
        InfoPanelView infoPanel;
        if (currentPlayer == gameModel.getPlayerOne()) {
            infoPanel = gameView.getInfoPanelViewOne();
        } else {
            infoPanel = gameView.getInfoPanelViewTwo();
        }

        infoPanel.getTotalClicksLabel().setText("Anzahl gesamter Klicks: " + currentPlayer.getPlayerStatus().getTotalClicks());
        infoPanel.getHitsLabel().setText("Davon Getroffen (Hits): " + currentPlayer.getPlayerStatus().getHits());
        infoPanel.getMissesLabel().setText("Verfehlt: " + currentPlayer.getPlayerStatus().getMisses());
        infoPanel.getSunkShipsLabel().setText("Gegnerische Schiffe versunken: " + currentPlayer.getPlayerStatus().getShunkShips());
    }

    private void showGameOverDialog() {
        String winner = gameModel.getCurrentPlayer().getPlayerName();
        JOptionPane.showMessageDialog(gameView, "Spiel vorbei! " + winner + " gewinnt!");
    }

    private class LocalGameButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            startGame(GameState.NORMAL);
        }
    }

    private class DebugModeButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            startGame(GameState.DEBUG);
        }
    }

    private class ExitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private class MainMenuButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showHomeScreen();
        }
    }

    private class PauseGameButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(gameView, "Spiel ist pausiert!");
        }
    }

    private class EndGameButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private class BoardClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            BoardView clickedBoard = (BoardView) e.getSource();
            int cellSize = clickedBoard.getCellSize();
            int x = e.getX() / cellSize;
            int y = e.getY() / cellSize;

            if (!gameModel.allShipsPlaced()) {
                boolean horizontal = SwingUtilities.isLeftMouseButton(e);
                handleShipPlacement(x, y, horizontal);
            } else {
                handlePlayerMove();
            }
        }
    }
    public void runGameLoop() {
        if (gameModel.getGameState() == GameState.NORMAL) {
            gameView.updateBoardVisibility(gameModel.getCurrentPlayer());
        } else {
            gameView.getPlayerBoardOne().setCovered(false);
            gameView.getPlayerBoardTwo().setCovered(false);
        }

        while (!gameModel.isGameOver()) {
            PlayerModel currentPlayer = gameModel.getCurrentPlayer();
            gameView.getStatusView().updateStatus(currentPlayer.getPlayerName() + " ist am Zug");

            if (currentPlayer instanceof ComputerPlayerModel) {
                gameModel.computerPlayTurn();
            } else {
                gameView.getPlayerBoardOne().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        BoardView clickedBoard = (BoardView) e.getSource();
                        int cellSize = clickedBoard.getCellSize();
                        int x = e.getX() / cellSize;
                        int y = e.getY() / cellSize;

                        gameModel.playerTurn(x, y);
                        updateGameView();

                        if (gameModel.isGameOver()) {
                            showGameOverDialog();
                        }
                    }
                });
            }

            updateGameView();

            if (!gameModel.isGameOver()) {
                gameModel.switchPlayer();
                // Nur im normalen Modus die Sichtbarkeit des Boards aktualisieren
                if (gameModel.getGameState() == GameState.NORMAL) {
                    gameView.updateBoardVisibility(gameModel.getCurrentPlayer());
                }
            } else {
                showGameOverDialog();
            }
        }
    }
}