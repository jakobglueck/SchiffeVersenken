package controller;

import model.*;
import View.*;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BoardController {

    private GameModel gameModel;
    private GameView gameView;
    private GameController gameController;

    public BoardController(GameModel gameModel, GameView gameView, GameController gameController) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.gameController = gameController;
    }

    public void initializeGameListeners() {
        gameView.getControlView().getMainMenuButton().addActionListener(e -> {
            this.gameModel.resetGame();
            gameController.showHomeScreen();
            gameController.initializeHomeScreenListeners();
        });
        gameView.getControlView().getPauseGameButton().addActionListener(e -> JOptionPane.showMessageDialog(gameView, "Spiel ist pausiert!"));
        gameView.getControlView().getEndGameButton().addActionListener(e -> System.exit(0));
        gameView.getPlayerBoardOne().setBoardClickListener(this::handleBoardClick);
        gameView.getPlayerBoardTwo().setBoardClickListener(this::handleBoardClick);
    }

    public void updateBoardVisibility() {
        PlayerModel currentPlayer = gameModel.getCurrentPlayer();
        gameView.updateBoardVisibility(currentPlayer);
    }

    public void toggleBoardsForCurrentPlayer() {
        if (gameModel.getCurrentPlayer() == gameModel.getPlayerOne()) {
            enableBoard(gameView.getPlayerBoardTwo());
            disableBoard(gameView.getPlayerBoardOne());
        } else {
            enableBoard(gameView.getPlayerBoardOne());
            disableBoard(gameView.getPlayerBoardTwo());
        }
    }

    public void enableBothBoards() {
        enableBoard(gameView.getPlayerBoardOne());
        enableBoard(gameView.getPlayerBoardTwo());
    }

    public void updateGameView() {
        gameView.getPlayerBoardOne().updateBoard();
        gameView.getPlayerBoardTwo().updateBoard();
        gameView.updateBoardVisibility(gameModel.getCurrentPlayer());
        gameView.getStatusView().updatePlayerName("Aktueller Spieler: " + gameModel.getCurrentPlayer().getPlayerName());
        gameView.getPlayerBoardOne().revalidate();
        gameView.getPlayerBoardOne().repaint();
        gameView.getPlayerBoardTwo().revalidate();
        gameView.getPlayerBoardTwo().repaint();
        gameView.getStatusView().revalidate();
        gameView.getStatusView().repaint();

        updateInfoPanel();
    }

    private void updateInfoPanel() {
        gameView.getInfoPanelViewOne().updateStats(gameModel.getPlayerOne());
        gameView.getInfoPanelViewTwo().updateStats(gameModel.getPlayerTwo());

        gameView.getInfoPanelViewOne().revalidate();
        gameView.getInfoPanelViewOne().repaint();
        gameView.getInfoPanelViewTwo().revalidate();
        gameView.getInfoPanelViewTwo().repaint();
    }

    private void handleBoardClick(int row, int col, JLabel label) {
        Component parent = label.getParent().getParent().getParent();

        BoardView clickedBoardView = (BoardView) parent;

        this.processBoardClick(row, col, clickedBoardView, label);

    }

    private void processBoardClick(int row, int col, BoardView clickedBoardView, JLabel label) {
        PlayerModel currentPlayer = gameModel.getCurrentPlayer();

        BoardView opponentBoardView = (currentPlayer == gameModel.getPlayerOne()) ? gameView.getPlayerBoardTwo()  : gameView.getPlayerBoardOne();

        if (gameModel.getGameState() == GameState.NORMAL && clickedBoardView != opponentBoardView) {
            gameView.getStatusView().updateAdditionalInfo(currentPlayer.getPlayerName() + " hat sein eigenes Board angegriffen. Bitte greife das Board des Gegners an!");
            return;
        }

        boolean hitShip = this.checkStatusOfClick(row, col, clickedBoardView, label);

        this.changeClickRow(clickedBoardView, hitShip);

        updateGameView();

    }
    private void changeClickRow(BoardView clickedBoardView, boolean hitShip){
        if (clickedBoardView.getPlayerBoard().allShipsAreHit()) {
            gameController.showGameOverDialog();
        } else {
            if (gameModel.getGameState() == GameState.NORMAL || gameModel.getGameState() == GameState.COMPUTER) {
                if (!hitShip) {
                    gameModel.switchPlayer();
                }
                if (!(gameModel.getCurrentPlayer() instanceof ComputerPlayerModel)) {
                    SwingUtilities.invokeLater(gameController::runGameLoop);
                } else {
                    gameController.performComputerMove();
                    updateGameView();
                }
            }
        }
    }

    private boolean checkStatusOfClick(int row, int col, BoardView clickedBoardView, JLabel label ){
        PlayerModel currentPlayer = gameModel.getCurrentPlayer();

        CellModel cell = clickedBoardView.getPlayerBoard().getCell(row, col);
        currentPlayer.getPlayerStatus().updateTotalClicks();
        boolean hitShip = false;

        switch (cell.getCellState()) {
            case FREE:
                clickedBoardView.markAsMiss(label);
                gameView.getStatusView().updateAdditionalInfo(currentPlayer.getPlayerName() + " hat nicht getroffen");
                break;
            case SET:
                ShipModel ship = clickedBoardView.getPlayerBoard().registerHit(row, col);
                currentPlayer.getPlayerStatus().calculateHits(clickedBoardView.getPlayerBoard());
                currentPlayer.getPlayerStatus().calculateShunkShips(clickedBoardView.getPlayerBoard());
                hitShip = true;
                gameView.getStatusView().updateAdditionalInfo(currentPlayer.getPlayerName() + " hat getroffen");
                if (ship != null && ship.isSunk()) {
                    clickedBoardView.updateRevealedShip(ship);
                    gameView.getStatusView().updateAdditionalInfo(currentPlayer.getPlayerName() + " hat ein Schiff versenkt");
                    markSurroundingCellsAsMiss(ship, clickedBoardView);
                }
                break;
            default:
                gameView.getStatusView().updateAdditionalInfo(currentPlayer.getPlayerName() + " kann ein bereits getroffenes Schiff nicht nochmal angreifen");
                return false;
        }
        return hitShip;
    }

    private void markSurroundingCellsAsMiss(ShipModel ship, BoardView opponent) {
        for (CellModel cell : ship.getShipCells()) {
            int startX = Math.max(0, cell.getX() - 1);
            int endX = Math.min(9, cell.getX() + 1);
            int startY = Math.max(0, cell.getY() - 1);
            int endY = Math.min(9, cell.getY() + 1);

            for (int x = startX; x <= endX; x++) {
                for (int y = startY; y <= endY; y++) {
                    CellModel surroundingCell = opponent.getPlayerBoard().getCell(x, y);
                    if (surroundingCell.getCellState() == CellState.FREE) {
                        JLabel surroundingLabel = opponent.getLabelForCell(x, y);
                        opponent.markAsMiss(surroundingLabel);
                    }
                }
            }
        }
    }

    private void enableBoard(BoardView board) {
        setBoardEnabled(board, true);
    }

    private void disableBoard(BoardView board) {
        setBoardEnabled(board, false);
    }

    private void setBoardEnabled(BoardView board, boolean enabled) {
        if (enabled) {
            for (MouseListener listener : board.getMouseListeners()) {
                board.removeMouseListener(listener);
            }
            board.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JLabel label = (JLabel) e.getSource();
                    int row = label.getY() / board.getCellSize();
                    int col = label.getX() / board.getCellSize();
                    handleBoardClick(row, col, label);
                }
            });
        } else {
            for (MouseListener listener : board.getMouseListeners()) {
                board.removeMouseListener(listener);
            }
        }
    }
}
