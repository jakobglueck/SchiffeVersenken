package controller;

import model.ComputerPlayerModel;
import model.GameModel;
import model.PlayerModel;
import model.ShipModel;
import model.CellModel;
import View.*;
import utils.CellState;
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
        initializeHomeScreenListeners();
    }

    private void initializeHomeScreenListeners() {
        homeScreenView.getLocalGameButton().addActionListener(e -> startGame(GameState.NORMAL));
        homeScreenView.getDebugModeButton().addActionListener(e -> startGame(GameState.DEBUG));
        homeScreenView.getExitButton().addActionListener(e -> System.exit(0));
    }

    private void initializeGameListeners() {
        gameView.getControlView().getMainMenuButton().addActionListener(e -> showHomeScreen());
        gameView.getControlView().getPauseGameButton().addActionListener(e -> JOptionPane.showMessageDialog(gameView, "Spiel ist pausiert!"));
        gameView.getControlView().getEndGameButton().addActionListener(e -> System.exit(0));
        gameView.getPlayerBoardOne().setBoardClickListener(this::handleBoardClick);
        gameView.getPlayerBoardTwo().setBoardClickListener(this::handleBoardClick);
    }

    public void showHomeScreen() {
        homeScreenView.setVisible(true);
        gameView.setVisible(false);
    }

    public void startGame(GameState gameState) {
        homeFrame.setVisible(false);
        String playerOneName = JOptionPane.showInputDialog("Bitte Namen für Spieler 1 eingeben:");
        String playerTwoName = (gameState == GameState.NORMAL || gameState == GameState.DEBUG)
                ? JOptionPane.showInputDialog("Bitte Namen für Spieler 2 eingeben:")
                : "Computer";

        gameModel.createPlayerWithNames(playerOneName, playerTwoName);
        gameModel.setGameState(gameState);
        gameView.setVisible(true);
        gameView.createPlayerBase();
        initializeGameListeners();
        gameModel.startGame();
        if (gameState == GameState.NORMAL || gameState == GameState.COMPUTER) {
            handleManualShipPlacement();
        } else {
            runGameLoop();
        }
    }

    public void runGameLoop() {
        this.gameView.updateBoardVisibility(gameModel.getCurrentPlayer());
        updateGameView();

        // Aktivieren Sie nur das Board des Gegners und deaktivieren Sie das eigene Board
        if (gameModel.getCurrentPlayer() == gameModel.getPlayerOne()) {
            setBoardEnabled(gameView.getPlayerBoardTwo(), true);
            setBoardEnabled(gameView.getPlayerBoardOne(), false);
        } else {
            setBoardEnabled(gameView.getPlayerBoardOne(), true);
            setBoardEnabled(gameView.getPlayerBoardTwo(), false);
        }
    }

    private void handleBoardClick(int row, int col, JLabel label) {
        PlayerModel currentPlayer = gameModel.getCurrentPlayer();
        PlayerModel opponent = (currentPlayer == gameModel.getPlayerOne()) ? gameModel.getPlayerTwo() : gameModel.getPlayerOne();
        BoardView opponentBoardView = (currentPlayer == gameModel.getPlayerOne()) ? gameView.getPlayerBoardTwo() : gameView.getPlayerBoardOne();

        CellModel cell = opponent.getBoard().getCell(row, col); // Hier wird das Board des Gegners abgefragt

        switch (cell.getCellState()) {
            case FREE:
                opponentBoardView.markAsMiss(label);
                opponent.getBoard().changeCellState(row, col, CellState.FREE);
                break;
            case SET:
                ShipModel ship = opponent.getBoard().registerHit(row, col);
                if (ship != null && ship.isSunk()) {
                    opponentBoardView.updateRevealedShip(ship);
                    markSurroundingCellsAsMiss(ship, opponentBoardView, opponent);
                }
                break;
            default:
                System.out.println("Ungültiger Klick.");
        }

        updateGameView();  // Aktualisierung der GUI nach jedem gültigen Zug

        if (opponent.getBoard().allShipsAreHit()) {
            showGameOverDialog();
        } else {
            gameModel.switchPlayer(); // Spielerwechsel nach jedem gültigen Zug
            SwingUtilities.invokeLater(() -> {
                runGameLoop();  // Stellen Sie sicher, dass nur das Board des nächsten Spielers aktiviert wird
            });
        }
    }

    private void setBoardEnabled(BoardView board, boolean enabled) {
        if (enabled) {
            for (MouseListener listener : board.getMouseListeners()) {
                board.removeMouseListener(listener);
            }
            board.addMouseListener(board.getBoardClickListener());
        } else {
            for (MouseListener listener : board.getMouseListeners()) {
                board.removeMouseListener(listener);
            }
        }
    }

    private void handleManualShipPlacement() {
        int shipTurns = (gameModel.getPlayerTwo() instanceof ComputerPlayerModel) ? 1 : 2;

        placeShipsForCurrentPlayer();

        if (shipTurns == 2) {
            gameModel.switchPlayer();
            placeShipsForCurrentPlayer();
        }

        JOptionPane.showMessageDialog(gameView, "Alle Schiffe platziert. Das Spiel beginnt jetzt!");
        gameView.getStatusView().updateStatus("Spiel beginnt!");
        runGameLoop();
    }

    private void placeShipsForCurrentPlayer() {
        PlayerModel currentPlayer = gameModel.getCurrentPlayer();
        gameView.getStatusView().updateStatus(currentPlayer.getPlayerName() + ", platziere deine Schiffe");

        for (MouseListener listener : gameView.getPlayerBoardOne().getMouseListeners()) {
            gameView.getPlayerBoardOne().removeMouseListener(listener);
        }

        gameView.getPlayerBoardOne().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / gameView.getPlayerBoardOne().getCellSize();
                int y = e.getY() / gameView.getPlayerBoardOne().getCellSize();
                boolean horizontal = SwingUtilities.isLeftMouseButton(e);

                if (gameModel.placeNextShip(x, y, horizontal)) {
                    updateGameView();
                    if (currentPlayer.allShipsPlaced()) {
                        gameView.getPlayerBoardOne().removeMouseListener(this);
                        if (gameModel.getCurrentPlayer() instanceof ComputerPlayerModel) {
                            gameModel.switchPlayer();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(gameView, "Ungültige Schiffsplatzierung. Versuche es erneut.");
                }
            }
        });
    }

    private void markSurroundingCellsAsMiss(ShipModel ship, BoardView opponentBoardView, PlayerModel opponent) {
        for (CellModel cell : ship.getShipCells()) {
            int startX = Math.max(0, cell.getX() - 1);
            int endX = Math.min(9, cell.getX() + 1);
            int startY = Math.max(0, cell.getY() - 1);
            int endY = Math.min(9, cell.getY() + 1);

            for (int x = startX; x <= endX; x++) {
                for (int y = startY; y <= endY; y++) {
                    CellModel surroundingCell = opponentBoardView.getPlayerBoard().getCell(x, y);
                    if (surroundingCell.getCellState() == CellState.FREE) {
                        JLabel surroundingLabel = opponentBoardView.getLabelForCell(x, y);
                        opponentBoardView.markAsMiss(surroundingLabel);
                        opponent.getBoard().changeCellState(x, y, CellState.FREE);
                    }
                }
            }
        }
    }

    private void updateGameView() {
        gameView.getPlayerBoardOne().updateBoard();
        gameView.getPlayerBoardTwo().updateBoard();
        gameView.updateBoardVisibility(gameModel.getCurrentPlayer());
        gameView.getStatusView().updateStatus("Aktueller Spieler: " + gameModel.getCurrentPlayer().getPlayerName());

        // Panels revalidate und repaint, um sicherzustellen, dass die Änderungen sichtbar werden
        gameView.getPlayerBoardOne().revalidate();
        gameView.getPlayerBoardOne().repaint();
        gameView.getPlayerBoardTwo().revalidate();
        gameView.getPlayerBoardTwo().repaint();
        gameView.getStatusView().revalidate();
        gameView.getStatusView().repaint();

        this.updateInfoPanel();
    }

    private void updateInfoPanel() {
        this.gameView.getInfoPanelViewOne().updateStats(this.gameModel.getPlayerOne());
        this.gameView.getInfoPanelViewTwo().updateStats(this.gameModel.getPlayerTwo());

        // Panels revalidate und repaint, um sicherzustellen, dass die Änderungen sichtbar werden
        gameView.getInfoPanelViewOne().revalidate();
        gameView.getInfoPanelViewOne().repaint();
        gameView.getInfoPanelViewTwo().revalidate();
        gameView.getInfoPanelViewTwo().repaint();
    }

    private void showGameOverDialog() {
        String winner = gameModel.getCurrentPlayer().getPlayerName();
        JOptionPane.showMessageDialog(gameView, "Spiel vorbei! " + winner + " gewinnt!");
    }
}
