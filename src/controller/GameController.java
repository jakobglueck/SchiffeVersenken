package controller;

import model.GameModel;
import model.PlayerModel;
import View.*;
import utils.GameState;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameController {

    private GameModel gameModel;
    private GameView gameView;
    private HomeScreenView homeScreenView;

    public GameController(GameModel gameModel, GameView gameView, HomeScreenView homeScreenView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.homeScreenView = homeScreenView;
        initializeListeners();
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
        gameModel.setGameState(gameState);
        gameModel.startGame();
        gameView.updateBoardVisibility(gameModel.getCurrentPlayer());

        if (gameState == GameState.NORMAL) {
            handleManualShipPlacement();
        } else if (gameState == GameState.COMPUTER) {
            handleManualShipPlacement();
            gameModel.getPlayerTwo().getBoard().placeAllShips();
        } else if (gameState == GameState.DEBUG) {
            gameModel.getPlayerOne().getBoard().placeAllShips();
            gameModel.getPlayerTwo().getBoard().placeAllShips();
        }

        updateGameView();
        homeScreenView.setVisible(false);
        gameView.setVisible(true);
    }

    private void handleManualShipPlacement() {
        PlayerModel currentPlayer = gameModel.getCurrentPlayer();
        while (!currentPlayer.allShipsPlaced()) {
            gameView.getStatusView().updateStatus(currentPlayer.getPlayerName() + ", platziere deine Schiffe");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handlePlayerMove(int x, int y) {
        if (!gameModel.isGameOver()) {
            gameModel.playerTurn(x, y);
            updateGameView();

            if (gameModel.getGameState() == GameState.COMPUTER && !gameModel.isGameOver()) {
                gameModel.computerPlayTurn();
                updateGameView();
            }
        }
        if (gameModel.isGameOver()) {
            showGameOverDialog();
        }
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
        updateInfoPanel();
    }

    private void updateInfoPanel() {
        PlayerModel currentPlayer = gameModel.getCurrentPlayer();
        InfoPanelView infoPanel = gameView.getInfoPanelViewOne();
        infoPanel.getTotalClicksLabel().setText("Anzahl gesamter Klicks: " + currentPlayer.getTotalClicks());
        infoPanel.getHitsLabel().setText("Davon Getroffen (Hits): " + currentPlayer.getHits());
        infoPanel.getMissesLabel().setText("Verfehlt: " + currentPlayer.getMisses());
        infoPanel.getSunkShipsLabel().setText("Gegnerische Schiffe versunken: " + currentPlayer.getSunkShips());
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
                handlePlayerMove(x, y);
            }
        }
    }
}