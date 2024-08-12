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
        gameView.updateBoardVisibility(gameModel.getCurrentPlayer());
        updateGameView();
        // Initialisiere den MouseListener einmalig
        MouseAdapter boardClickListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BoardView opponentBoardView = (gameModel.getCurrentPlayer() == gameModel.getPlayerOne())
                        ? gameView.getPlayerBoardTwo()
                        : gameView.getPlayerBoardOne();

                int x = e.getX() / opponentBoardView.getCellSize();
                int y = e.getY() / opponentBoardView.getCellSize();

                // Spieler macht einen Zug
                gameModel.playerTurn(x, y);
                updateGameView();

                if (gameModel.isGameOver()) {
                    showGameOverDialog();
                    return;
                }

                // Wechsel zum nächsten Spieler
                gameModel.switchPlayer();
                gameView.updateBoardVisibility(gameModel.getCurrentPlayer());

                // Wenn der nächste Spieler ein Computer ist, führt der Computer seinen Zug durch
                if (gameModel.getCurrentPlayer() instanceof ComputerPlayerModel) {
                    gameModel.computerPlayTurn();
                    updateGameView();

                    if (gameModel.isGameOver()) {
                        showGameOverDialog();
                    } else {
                        // Nach dem Computerzug wieder zum menschlichen Spieler wechseln
                        gameModel.switchPlayer();
                        gameView.updateBoardVisibility(gameModel.getCurrentPlayer());
                    }
                }
            }
        };

        // Füge den Listener für den menschlichen Spieler einmalig hinzu
        gameView.getPlayerBoardOne().addMouseListener(boardClickListener);
        gameView.getPlayerBoardTwo().addMouseListener(boardClickListener);

        // Falls das Spiel mit einem Computer startet, kann der Computer sofort seinen Zug machen
        if (gameModel.getCurrentPlayer() instanceof ComputerPlayerModel) {
            gameModel.computerPlayTurn();
            updateGameView();

            if (gameModel.isGameOver()) {
                showGameOverDialog();
            } else {
                gameModel.switchPlayer();
                gameView.updateBoardVisibility(gameModel.getCurrentPlayer());
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

    private void handleBoardClick(int row, int col, JLabel label) {
        PlayerModel currentPlayer = gameModel.getCurrentPlayer();
        PlayerModel opponent = (currentPlayer == gameModel.getPlayerOne()) ? gameModel.getPlayerTwo() : gameModel.getPlayerOne();
        BoardView opponentBoardView = (currentPlayer == gameModel.getPlayerOne()) ? gameView.getPlayerBoardTwo() : gameView.getPlayerBoardOne();

        CellModel cell = opponent.getBoard().getCell(row, col);

        switch (cell.getCellState()) {
            case FREE:
                opponentBoardView.markAsMiss(label);
                opponent.getBoard().changeCellState(row, col, CellState.FREE);
                break;
            case SET:
                ShipModel ship = opponent.getBoard().registerHit(row, col);
                if (ship != null && ship.isSunk()) {
                    opponentBoardView.updateRevealedShip(ship);
                    markSurroundingCellsAsMiss(ship, opponentBoardView,opponent);
                }
                break;
            default:
                System.out.println("Ungültiger Klick.");
        }

        if (opponent.getBoard().allShipsAreHit()) {
            showGameOverDialog();
        } else {
            updateGameView();
        }
    }

    private void markSurroundingCellsAsMiss(ShipModel ship, BoardView opponentBoardView, PlayerModel opponent) {
        for (CellModel cell : ship.getShipCells()) {
            int startX = Math.max(0, cell.getX() - 1);
            int endX = Math.min(9, cell.getX() + 1);
            int startY = Math.max(0, cell.getY() - 1);
            int endY = Math.min(9, cell.getY() + 1);

            for (int x = startX; x <= endX; x++) {
                for (int y = startY; y <= endY; y++) {
                    CellModel surroundingCell = opponentBoardView.getPlayerBoard().getCell(x, y);  // <-- Korrigiert
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
        this.updateInfoPanel();
    }

    private void updateInfoPanel() {
        this.gameView.getInfoPanelViewOne().updateStats(this.gameModel.getPlayerOne());
        this.gameView.getInfoPanelViewTwo().updateStats(this.gameModel.getPlayerTwo());
    }

    private void showGameOverDialog() {
        String winner = gameModel.getCurrentPlayer().getPlayerName();
        JOptionPane.showMessageDialog(gameView, "Spiel vorbei! " + winner + " gewinnt!");
    }
}
