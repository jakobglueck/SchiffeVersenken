/**
 * @file BoardController.java
 * @brief Diese Klasse verwaltet die Logik der Spielfelder und deren Interaktionen im Spiel.
 */

package controller;

import model.*;
import view.*;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @class BoardController
 * @brief Verantwortlich für die Steuerung und Aktualisierung der Spielfelder im Spiel.
 *
 * Diese Klasse koordiniert die Interaktionen zwischen dem Spielmodell (GameModel) und
 * der Spielansicht (GameView) in Bezug auf die Spielfelder. Sie verarbeitet Benutzerinteraktionen
 * mit den Spielfeldern und aktualisiert den Spielzustand entsprechend.
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
     *
     * Setzt Event-Listener für Steuerungsbuttons und Spielfeldklicks. Initialisiert auch
     * die initiale Spielansicht.
     */
    public void startGameListeners() {
        this.gameView.getGameControlView().getPauseGameButton().addActionListener(e -> JOptionPane.showMessageDialog(gameView, "Spiel ist pausiert!"));
        this.gameView.getGameControlView().getEndGameButton().addActionListener(e -> System.exit(0));
        this.gameView.getPlayerBoardOne().setBoardClickListener(this::handleBoardClick);
        this.gameView.getPlayerBoardTwo().setBoardClickListener(this::handleBoardClick);
        this.updateGameView();
    }

    /**
     * @brief Aktualisiert die Spielansicht, einschließlich der Spielfelder und Statusinformationen.
     *
     * Ruft verschiedene Update-Methoden auf, um alle Aspekte der Spielansicht zu aktualisieren.
     */
    public void updateGameView() {
        this.updateBoards();
        this.updateBoardVisibility();
        this.updateStatusView();
        this.updateInfoPanel();
    }

    /**
     * @brief Aktualisiert die Darstellung beider Spielfelder.
     *
     * Aktualisiert die visuelle Darstellung der Spielfelder basierend auf dem aktuellen Spielzustand.
     */
    private void updateBoards() {
        this.gameView.getPlayerBoardOne().updateBoard(this.gameModel.getPlayerOne().getBoard());
        this.gameView.getPlayerBoardTwo().updateBoard(this.gameModel.getPlayerTwo().getBoard());
        this.gameView.getPlayerBoardOne().revalidate();
        this.gameView.getPlayerBoardOne().repaint();
        this.gameView.getPlayerBoardTwo().revalidate();
        this.gameView.getPlayerBoardTwo().repaint();
    }

    /**
     * @brief Aktualisiert die Sichtbarkeit der Spielfelder basierend auf dem aktuellen Spieler.
     *
     * Passt die Sichtbarkeit der Spielfelder an den aktuellen Spielzustand und Spieler an.
     */
    public void updateBoardVisibility() {
        this.gameView.updateBoardVisibility(this.gameModel, this.gameModel.getGameState());
    }

    /**
     * @brief Aktualisiert die Statusanzeige mit dem Namen des aktuellen Spielers.
     */
    private void updateStatusView() {
        this.gameView.getGameInfoView().updatePlayerName("Aktueller Spieler: " + this.gameModel.getCurrentPlayer().getPlayerName());
        this.gameView.getGameInfoView().revalidate();
        this.gameView.getGameInfoView().repaint();
    }

    /**
     * @brief Aktualisiert die Informationspanels mit den Statistiken der Spieler.
     *
     * Aktualisiert die Statistiken beider Spieler in den entsprechenden Info-Panels.
     */
    private void updateInfoPanel() {
        this.gameView.getStatsViewOne().updateStats(this.gameModel.getPlayerOne());
        this.gameView.getStatsViewTwo().updateStats(this.gameModel.getPlayerTwo());
        this.gameView.getStatsViewOne().revalidate();
        this.gameView.getStatsViewOne().repaint();
        this.gameView.getStatsViewTwo().revalidate();
        this.gameView.getStatsViewTwo().repaint();
    }

    /**
     * @brief Schaltet die Spielfelder für den aktuellen Spieler um.
     *
     * Aktiviert das Spielfeld des Gegners und deaktiviert das eigene Spielfeld für den aktuellen Spieler.
     */
    public void toggleBoardsForCurrentPlayer() {
        if (this.gameModel.getCurrentPlayer() == this.gameModel.getPlayerOne()) {
            this.enableBoard(this.gameView.getPlayerBoardTwo());
            this.disableBoard(this.gameView.getPlayerBoardOne());
        } else {
            this.enableBoard(this.gameView.getPlayerBoardOne());
            this.disableBoard(this.gameView.getPlayerBoardTwo());
        }
    }

    /**
     * @brief Aktiviert beide Spielfelder, beispielsweise im Debug-Modus.
     */
    public void enableBothBoards() {
        this.enableBoard(this.gameView.getPlayerBoardOne());
        this.enableBoard(this.gameView.getPlayerBoardTwo());
    }

    /**
     * @brief Aktiviert das angegebene Spielfeld für Interaktionen.
     * @param board Das Spielfeld, das aktiviert werden soll.
     */
    private void enableBoard(BoardView board) {
        this.setBoardEnabled(board, true);
    }

    /**
     * @brief Deaktiviert das angegebene Spielfeld für Interaktionen.
     * @param board Das Spielfeld, das deaktiviert werden soll.
     */
    private void disableBoard(BoardView board) {
        this.setBoardEnabled(board, false);
    }

    /**
     * @brief Aktiviert oder deaktiviert das angegebene Spielfeld basierend auf dem übergebenen Status.
     * @param board Das Spielfeld, das aktiviert oder deaktiviert werden soll.
     * @param enabled Gibt an, ob das Spielfeld aktiviert (true) oder deaktiviert (false) werden soll.
     *
     * Fügt einen MouseListener hinzu oder entfernt ihn, je nach dem gewünschten Zustand.
     * Behandelt auch Ausnahmen für Klicks außerhalb des gültigen Spielfeldbereichs.
     */
    private void setBoardEnabled(BoardView board, boolean enabled) {
        if (enabled) {
            for (MouseListener listener : board.getMouseListeners()) {
                board.removeMouseListener(listener);
            }
            board.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        JLabel label = (JLabel) e.getSource();
                        int row = label.getY() / board.getCellSize();
                        int col = label.getX() / board.getCellSize();
                        handleBoardClick(row, col, label);
                    } catch (Exception exception) {
                        PlayerModel currentPlayer = gameModel.getCurrentPlayer();
                        gameView.getGameInfoView().updateStatusMessageLabel(currentPlayer.getPlayerName() + " bitte klicke auf das Board und nicht auf die Beschriftung des Boards!");
                    }
                }
            });
        } else {
            for (MouseListener listener : board.getMouseListeners()) {
                board.removeMouseListener(listener);
            }
        }
    }

    /**
     * @brief Verarbeitet Klicks auf dem Spielfeld und leitet die notwendige Logik ein.
     * @param row Die angeklickte Zeile.
     * @param col Die angeklickte Spalte.
     * @param label Das JLabel des angeklickten Feldes.
     *
     * Überprüft die Gültigkeit des Klicks und leitet die Verarbeitung an processBoardClick weiter.
     */
    private void handleBoardClick(int row, int col, JLabel label) {
        Component parent = label.getParent().getParent().getParent();
        if (!(parent instanceof BoardView)) {
            throw new IllegalArgumentException("Klick außerhalb des Spielfelds");
        }
        BoardView clickedBoardView = (BoardView) parent;
        this.processBoardClick(row, col, clickedBoardView, label);
    }

    /**
     * @brief Ermittelt das BoardModel für eine gegebene BoardView.
     * @param boardView Die BoardView, für die das BoardModel ermittelt werden soll.
     * @return Das zugehörige BoardModel.
     */
    private BoardModel getBoardModelForView(BoardView boardView) {
        return (boardView == this.gameView.getPlayerBoardOne()) ? this.gameModel.getPlayerOne().getBoard() : this.gameModel.getPlayerTwo().getBoard();
    }

    /**
     * @brief Verarbeitet den Spielfeld-Klick, führt die nötigen Aktionen aus und aktualisiert die Ansicht.
     * @param row Die angeklickte Zeile.
     * @param col Die angeklickte Spalte.
     * @param clickedBoardView Die Ansicht des angeklickten Spielfelds.
     * @param label Das JLabel des angeklickten Feldes.
     *
     * Führt die Hauptlogik für einen Spielzug aus, einschließlich der Überprüfung auf Treffer
     * und der Aktualisierung des Spielzustands.
     */
    private void processBoardClick(int row, int col, BoardView clickedBoardView, JLabel label) {
        PlayerModel currentPlayer = this.gameModel.getCurrentPlayer();
        BoardModel currentBoardModel = currentPlayer.getBoard();
        BoardModel clickedBoard = this.getBoardModelForView(clickedBoardView);

        if (this.gameModel.getGameState() == GameState.NORMAL && clickedBoard == currentBoardModel) {
            this.gameView.getGameInfoView().updateStatusMessageLabel(currentPlayer.getPlayerName() + " greife das Board des Gegners an!");
            return;
        }

        boolean hitShip = this.checkStatusOfClick(row, col, clickedBoardView, clickedBoard, label);
        this.changeClickRow(hitShip);
        this.updateGameView();
    }

    /**
     * @brief Verarbeitet den Status des angeklickten Feldes und wechselt gegebenenfalls den Spieler.
     * @param hitShip Gibt an, ob ein Schiff getroffen wurde.
     *
     * Überprüft, ob das Spiel beendet ist, wechselt den Spieler wenn nötig und
     * initiiert den nächsten Zug oder Computerzug.
     */
    private void changeClickRow(boolean hitShip) {
        BoardModel opponentBoard = (this.gameModel.getCurrentPlayer() == this.gameModel.getPlayerOne()) ?
                this.gameModel.getPlayerTwo().getBoard() : this.gameModel.getPlayerOne().getBoard();
        if (opponentBoard.allShipsAreHit()) {
            this.gameController.showGameOverScreen();
        } else {
            if (this.gameModel.getGameState() == GameState.NORMAL || this.gameModel.getGameState() == GameState.COMPUTER) {
                if (!hitShip) {
                    this.gameModel.switchPlayer();
                }
                if (!(this.gameModel.getCurrentPlayer() instanceof ComputerPlayerModel)) {
                    SwingUtilities.invokeLater(this.gameController::runGameLoop);
                } else {
                    this.gameController.makeComputerMove();
                    this.updateGameView();
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
     *
     * Verarbeitet den Klick auf ein Feld, aktualisiert den Spielstatus und die Ansicht entsprechend.
     */
    private boolean checkStatusOfClick(int row, int col, BoardView clickedBoardView, BoardModel opponentBoardModel, JLabel label) {
        PlayerModel currentPlayer = this.gameModel.getCurrentPlayer();
        CellModel cell = opponentBoardModel.getCell(row, col);
        currentPlayer.getPlayerStatus().updateTotalClicks();
        boolean hitShip = false;

        switch (cell.getCellState()) {
            case FREE:
                clickedBoardView.markAsMiss(label);
                this.gameView.getGameInfoView().updateStatusMessageLabel(currentPlayer.getPlayerName() + " hat nicht getroffen");
                break;
            case SET:
                ShipModel ship = opponentBoardModel.registerHit(row, col);
                currentPlayer.getPlayerStatus().calculateHits(opponentBoardModel);
                currentPlayer.getPlayerStatus().calculateShunkShips(opponentBoardModel);
                hitShip = true;
                this.gameView.getGameInfoView().updateStatusMessageLabel(currentPlayer.getPlayerName() + " hat getroffen");
                if (ship != null && ship.isSunk()) {
                    clickedBoardView.updateRevealedShip(ship);
                    this.gameView.getGameInfoView().updateStatusMessageLabel(currentPlayer.getPlayerName() + " hat ein Schiff versenkt");
                    this.markSurroundingCellsAsMiss(ship, clickedBoardView, opponentBoardModel);
                }
                break;
            default:
                this.gameView.getGameInfoView().updateStatusMessageLabel(currentPlayer.getPlayerName() + " kann ein bereits getroffenes Schiff nicht nochmal angreifen");
                return false;
        }
        return hitShip;
    }

    /**
     * @brief Markiert die umgebenden Felder eines versenkten Schiffes als verfehlt.
     * @param ship Das versenkte Schiff.
     * @param opponent Die Ansicht des gegnerischen Spielfeldes.
     * @param opponentBoardModel Das BoardModel des Gegners.
     *
     * Markiert alle Felder um ein versenktes Schiff herum als Fehlschüsse,
     * um anzuzeigen, dass dort keine weiteren Schiffe sein können.
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
}