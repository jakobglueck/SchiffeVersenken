/**
 * @file BoardController.java
 * @brief Diese Klasse verwaltet die Logik der Spielfelder und deren Interaktionen im Spiel.
 */

package controller;

import model.*;
import View.*;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @class BoardController
 * @brief Verantwortlich für die Steuerung und Aktualisierung der Spielfelder im Spiel.
 */
public class BoardController {

    private GameModel gameModel; ///< Das Modell, das den Zustand des Spiels hält.
    private GameView gameView; ///< Die Ansicht, die die grafische Benutzeroberfläche des Spiels darstellt.
    private GameController gameController; ///< Der übergeordnete Controller, der das Spiel steuert.

    /**
     * @brief Konstruktor, der den BoardController initialisiert.
     * @param gameModel Das Modell des Spiels.
     * @param gameView Die Ansicht des Spiels.
     * @param gameController Der übergeordnete GameController.
     */
    public BoardController(GameModel gameModel, GameView gameView, GameController gameController) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.gameController = gameController;
    }

    /**
     * @brief Aktiviert die notwendigen Listener für die Spielfeldinteraktionen und Steuerungselemente.
     */
    public void startGameListeners() {
        gameView.getControlView().getMainMenuButton().addActionListener(e -> gameController.resetGame());
        gameView.getControlView().getPauseGameButton().addActionListener(e -> JOptionPane.showMessageDialog(gameView, "Spiel ist pausiert!"));
        gameView.getControlView().getEndGameButton().addActionListener(e -> System.exit(0));
        gameView.getPlayerBoardOne().setBoardClickListener(this::handleBoardClick);
        gameView.getPlayerBoardTwo().setBoardClickListener(this::handleBoardClick);
        updateGameView();
    }

    /**
     * @brief Aktualisiert die Sichtbarkeit der Spielfelder basierend auf dem aktuellen Spieler.
     */
    public void updateBoardVisibility() {
        gameView.updateBoardVisibility(gameModel, gameModel.getGameState());
    }

    /**
     * @brief Schaltet die Spielfelder für den aktuellen Spieler um.
     */
    public void toggleBoardsForCurrentPlayer() {
        if (gameModel.getCurrentPlayer() == gameModel.getPlayerOne()) {
            enableBoard(gameView.getPlayerBoardTwo());
            disableBoard(gameView.getPlayerBoardOne());
        } else {
            enableBoard(gameView.getPlayerBoardOne());
            disableBoard(gameView.getPlayerBoardTwo());
        }
    }

    /**
     * @brief Aktiviert beide Spielfelder, beispielsweise im Debug-Modus.
     */
    public void enableBothBoards() {
        enableBoard(gameView.getPlayerBoardOne());
        enableBoard(gameView.getPlayerBoardTwo());
    }

    /**
     * @brief Aktualisiert die Spielansicht, einschließlich der Spielfelder und Statusinformationen.
     */
    public void updateGameView() {
        gameView.getPlayerBoardOne().updateBoard(gameModel.getPlayerOne().getBoard());
        gameView.getPlayerBoardTwo().updateBoard(gameModel.getPlayerTwo().getBoard());
        gameView.updateBoardVisibility(gameModel, gameModel.getGameState());
        gameView.getPlayerBoardOne().revalidate();
        gameView.getPlayerBoardOne().repaint();
        gameView.getPlayerBoardTwo().revalidate();
        gameView.getPlayerBoardTwo().repaint();

        gameView.getStatusView().updatePlayerName("Aktueller Spieler: " + gameModel.getCurrentPlayer().getPlayerName());
        gameView.getStatusView().revalidate();
        gameView.getStatusView().repaint();

        updateInfoPanel();
    }

    /**
     * @brief Aktualisiert die Informationspanels mit den Statistiken der Spieler.
     */
    private void updateInfoPanel() {
        gameView.getInfoPanelViewOne().updateStats(gameModel.getPlayerOne());
        gameView.getInfoPanelViewTwo().updateStats(gameModel.getPlayerTwo());
        gameView.getInfoPanelViewOne().revalidate();
        gameView.getInfoPanelViewOne().repaint();
        gameView.getInfoPanelViewTwo().revalidate();
        gameView.getInfoPanelViewTwo().repaint();
    }

    /**
     * @brief Verarbeitet Klicks auf dem Spielfeld und leitet die notwendige Logik ein.
     * @param row Die angeklickte Zeile.
     * @param col Die angeklickte Spalte.
     * @param label Das JLabel des angeklickten Feldes.
     */
    private void handleBoardClick(int row, int col, JLabel label) {
        Component parent = label.getParent().getParent().getParent();
        BoardView clickedBoardView = (BoardView) parent;

        this.processBoardClick(row, col, clickedBoardView, label);
    }

    private BoardModel getBoardModelForView(BoardView boardView) {
        return (boardView == gameView.getPlayerBoardOne()) ? gameModel.getPlayerOne().getBoard() : gameModel.getPlayerTwo().getBoard();
    }

    /**
     * @brief Verarbeitet den Spielfeld-Klick, führt die nötigen Aktionen aus und aktualisiert die Ansicht.
     * @param row Die angeklickte Zeile.
     * @param col Die angeklickte Spalte.
     * @param clickedBoardView Die Ansicht des angeklickten Spielfelds.
     * @param label Das JLabel des angeklickten Feldes.
     */
    private void processBoardClick(int row, int col, BoardView clickedBoardView, JLabel label) {
        PlayerModel currentPlayer = gameModel.getCurrentPlayer();
        BoardModel currentBoardModel = currentPlayer.getBoard();
        BoardModel clickedBoard = this.getBoardModelForView(clickedBoardView);

        if (gameModel.getGameState() == GameState.NORMAL && clickedBord == currentBoardModel) {
            gameView.getStatusView().updateStatusMessageLabel(currentPlayer.getPlayerName() + " hat sein eigenes Board angegriffen. Bitte greife das Board des Gegners an!");
            return;
        }

        boolean hitShip = this.checkStatusOfClick(row, col, clickedBoardView, clickedBoard, label);
        this.changeClickRow(hitShip);
        updateGameView();
    }

    /**
     * @brief Verarbeitet den Status des angeklickten Feldes und wechselt gegebenenfalls den Spieler.
     * @param hitShip Gibt an, ob ein Schiff getroffen wurde.
     */
    private void changeClickRow(boolean hitShip) {
        BoardModel opponentBoard = (gameModel.getCurrentPlayer() == gameModel.getPlayerOne()) ? gameModel.getPlayerTwo().getBoard() : gameModel.getPlayerOne().getBoard();
        if (opponentBoard.allShipsAreHit()) {
            gameController.showGameOverScreen();
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

    /**
     * @brief Überprüft den Status des angeklickten Feldes und markiert Treffer oder Fehlversuche.
     * @param row Die angeklickte Zeile.
     * @param col Die angeklickte Spalte.
     * @param clickedBoardView Die Ansicht des angeklickten Spielfelds.
     * @param opponentBoardModel Das BoardModel des Gegners.
     * @param label Das JLabel des angeklickten Feldes.
     * @return true, wenn ein Schiff getroffen wurde; false sonst.
     */
    private boolean checkStatusOfClick(int row, int col, BoardView clickedBoardView, BoardModel opponentBoardModel, JLabel label) {
        PlayerModel currentPlayer = gameModel.getCurrentPlayer();
        CellModel cell = opponentBoardModel.getCell(row, col);
        currentPlayer.getPlayerStatus().updateTotalClicks();
        boolean hitShip = false;

        switch (cell.getCellState()) {
            case FREE:
                clickedBoardView.markAsMiss(label);
                gameView.getStatusView().updateStatusMessageLabel(currentPlayer.getPlayerName() + " hat nicht getroffen");
                break;
            case SET:
                ShipModel ship = opponentBoardModel.registerHit(row, col);
                currentPlayer.getPlayerStatus().calculateHits(opponentBoardModel);
                currentPlayer.getPlayerStatus().calculateShunkShips(opponentBoardModel);
                hitShip = true;
                gameView.getStatusView().updateStatusMessageLabel(currentPlayer.getPlayerName() + " hat getroffen");
                if (ship != null && ship.isSunk()) {
                    clickedBoardView.updateRevealedShip(ship);
                    gameView.getStatusView().updateStatusMessageLabel(currentPlayer.getPlayerName() + " hat ein Schiff versenkt");
                    markSurroundingCellsAsMiss(ship, clickedBoardView, opponentBoardModel);
                }
                break;
            default:
                gameView.getStatusView().updateStatusMessageLabel(currentPlayer.getPlayerName() + " kann ein bereits getroffenes Schiff nicht nochmal angreifen");
                return false;
        }
        return hitShip;
    }

    /**
     * @brief Markiert die umgebenden Felder eines versenkten Schiffes als verfehlt.
     * @param ship Das versenkte Schiff.
     * @param opponent Die Ansicht des gegnerischen Spielfeldes.
     * @param opponentBoardModel Das BoardModel des Gegners.
     */
    private void markSurroundingCellsAsMiss(ShipModel ship, BoardView opponent, BoardModel opponentBoardModel) {
        for (CellModel cell : ship.getShipCells()) {
            int startX = Math.max(0, cell.getX() - 1);
            int endX = Math.min(9, cell.getX() + 1);
            int startY = Math.max(0, cell.getY() - 1);
            int endY = Math.min(9, cell.getY() + 1);

            for (int x = startX; x <= endX; x++) {
                for (int y = startY; y <= endY; y++) {
                    CellModel surroundingCell = opponentBoardModel.getCell(x, y);
                    if (surroundingCell.getCellState() == CellState.FREE) {
                        JLabel surroundingLabel = opponent.getLabelForCell(x, y);
                        opponent.markAsMiss(surroundingLabel);
                    }
                }
            }
        }
    }

    /**
     * @brief Aktiviert das angegebene Spielfeld für Interaktionen.
     * @param board Das Spielfeld, das aktiviert werden soll.
     */
    private void enableBoard(BoardView board) {
        setBoardEnabled(board, true);
    }

    /**
     * @brief Deaktiviert das angegebene Spielfeld für Interaktionen.
     * @param board Das Spielfeld, das deaktiviert werden soll.
     */
    private void disableBoard(BoardView board) {
        setBoardEnabled(board, false);
    }

    /**
     * @brief Aktiviert oder deaktiviert das angegebene Spielfeld basierend auf dem übergebenen Status.
     * @param board Das Spielfeld, das aktiviert oder deaktiviert werden soll.
     * @param enabled Gibt an, ob das Spielfeld aktiviert (true) oder deaktiviert (false) werden soll.
     */
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

    /**
     * @brief Setzt den BoardController zurück.
     */
    public void reset() {
        updateBoardVisibility();

        removeAllBoardClickListeners(gameView.getPlayerBoardOne());
        removeAllBoardClickListeners(gameView.getPlayerBoardTwo());

        gameView.getPlayerBoardOne().setBoardClickListener(this::handleBoardClick);
        gameView.getPlayerBoardTwo().setBoardClickListener(this::handleBoardClick);

        gameView.getPlayerBoardOne().resetBoard();
        gameView.getPlayerBoardTwo().resetBoard();

        updateGameView();
    }

    private void removeAllBoardClickListeners(BoardView boardView) {
        for (MouseListener listener : boardView.getMouseListeners()) {
            boardView.removeMouseListener(listener);
        }
    }
}