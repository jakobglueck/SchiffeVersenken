/**
 * @file BoardController.java
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
 * Diese Klasse koordiniert die Interaktionen von Spielern mit den Boards. Dabei werden Daten aus GameModel geholt und
 * verändert und in der GameView angezeigt. Damit aktualisiert den Spielzustand.
 */
public class BoardController {
    // Instanz die Daten hält
    private GameModel gameModel;
    // Instanz der Spielansicht
    private GameView gameView;
    //übergeordnete Controller
    private GameController gameController;

    /**
     * @brief Konstruktor, der den BoardController initialisiert.
     * @param gameModel Die Daten des Spiels.
     * @param gameView Die Ansicht des Spiels.
     * @param gameController Der übergeordnete GameController.
     */
    public BoardController(GameModel gameModel, GameView gameView, GameController gameController) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.gameController = gameController;
    }

    /**
     * @brief Aktiviert die ActionListener  für die Spielfeldinteraktionen und Steuerungselemente.
     *
     * Setzt die Event-Listener für Steuerungsbuttons und Spielfeldklicks. Initialisiert auch
     * die Spielansicht.
     */
    public void startGameListeners() {
        this.gameView.getGameControlView().getPauseGameButton().addActionListener(e -> JOptionPane.showMessageDialog(gameView, "Spiel ist pausiert!"));
        this.gameView.getGameControlView().getEndGameButton().addActionListener(e -> System.exit(0));
        this.gameView.getPlayerBoardOne().setBoardClickListener(this::handleBoardClick);
        this.gameView.getPlayerBoardTwo().setBoardClickListener(this::handleBoardClick);
        this.updateGameView();
    }

    /**
     * @brief Aktualisiert die komplette Spielansicht.
     *
     * Ruft Update-Methoden auf, um alle Einheiten der Spielansicht zu aktualisieren.
     */
    public void updateGameView() {
        this.updateBoards();
        this.updateBoardVisibility();
        this.updateStatusView();
        this.updateInfoPanel();
    }

    /**
     * @brief Aktualisiert die Darstellung beider Boards.
     *
     * Aktualisiert die Darstellung der Boards mit den Daten aus dem GameModel.
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
     * @brief Aktualisiert die Anzeige der Statistik der Spieler.
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
     * @brief Aktiviert beide Spielfelder für den Debug-Modus.
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
     * @brief Aktiviert oder deaktiviert das angegebene Spielfeld basierend auf dem  GameStatus.
     * @param board Das Spielfeld, das aktiviert oder deaktiviert werden soll.
     * @param enabled Gibt an, ob das Spielfeld aktiviert oder deaktiviert werden soll.
     *
     * Fügt einen MouseListener hinzu oder entfernt ihn, je nach dem gewünschten Zustand.
     * Behandelt auch Ausnahmen für Klicks außerhalb des gültigen Spielfeldbereichs.
     */
    private void setBoardEnabled(BoardView board, boolean enabled) {
        if (enabled) {
            // entfernt zunächst alle MouseListener von dem Board
            for (MouseListener listener : board.getMouseListeners()) {
                board.removeMouseListener(listener);
            }
            // Fügt MouseListener zum Board hinzu
            board.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        JLabel label = (JLabel) e.getSource();
                        int row = label.getY() / board.getCellSize();
                        int col = label.getX() / board.getCellSize();
                        // Bekommt die X- und Y-Koordinate durch den Mausklick und übergibt sie der
                        handleBoardClick(row, col, label);
                    } catch (Exception exception) {
                        /*der Spieler hat nicht auf die Boards, sondern auf die numerische und alphabetsiche
                        Legende oberhalb und rechts vom Board geklickt.*/
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
     * @brief Verarbeitet Klicks auf dem Spielfeld und leitet weitere Logik.
     * @param row Die angeklickte Zeile.
     * @param col Die angeklickte Spalte.
     * @param label Das JLabel des angeklickten Feldes.
     *
     * Überprüft die Gültigkeit des Klicks und leitet die Verarbeitung an processBoardClick weiter.
     */
    private void handleBoardClick(int row, int col, JLabel label) {
        // getParent() ermittelt Boardview auf die geklickt wurde.
        Component parent = label.getParent().getParent().getParent();
        if (!(parent instanceof BoardView)) {
            // Nutzer hat ausserhalb des Boards geklickt
            throw new IllegalArgumentException("Klick außerhalb des Spielfelds");
        }
        BoardView clickedBoardView = (BoardView) parent;
        // Logik zum Umgang mit dem MausEvent
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
        // Verarbeitung des Mausklicks
        boolean hitShip = this.checkStatusOfClick(row, col, clickedBoardView, clickedBoard, label);
        // Entscheidet die Spielerwechsel
        this.changeClickRow(hitShip);
        // Spielansicht wird aktualisiert
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
        // Aktualisiert die Gesamtanzahl der Klicks für einen Spieler
        currentPlayer.getPlayerStatus().updateTotalClicks();
        boolean hitShip = false;

        switch (cell.getCellState()) {
            case FREE:
                // Spieler hat ein Schiff verfehlt
                clickedBoardView.markAsMiss(label);
                this.gameView.getGameInfoView().updateStatusMessageLabel(currentPlayer.getPlayerName() + " hat nicht getroffen");
                break;
            case SET:
                // Gibt an welches Schiff getroffen wurde.
                ShipModel ship = opponentBoardModel.registerHit(row, col);
                // Aktualisiert der restlichen Statistiken der Klicks für einen Spieler
                currentPlayer.getPlayerStatus().calculateHits(opponentBoardModel);
                currentPlayer.getPlayerStatus().calculateShunkShips(opponentBoardModel);

                hitShip = true;
                this.gameView.getGameInfoView().updateStatusMessageLabel(currentPlayer.getPlayerName() + " hat getroffen");
                if (ship != null && ship.isSunk()) {
                    // Deckt die Zellen um das Schiff auf, wenn es gesunken ist
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
    public void markSurroundingCellsAsMiss(ShipModel ship, BoardView opponent, BoardModel opponentBoardModel) {
        for (CellModel cell : ship.getShipCells()) {

            int startX = Math.max(0, cell.getX() - 1);
            int endX = Math.min(9, cell.getX() + 1);
            int startY = Math.max(0, cell.getY() - 1);
            int endY = Math.min(9, cell.getY() + 1);

            for (int x = startX; x <= endX; x++) {
                for (int y = startY; y <= endY; y++) {
                    CellModel surroundingCell = opponentBoardModel.getCell(x, y);
                    // Wenn die Zelle um die Schiffszelle kein Schiff ist, wird sie aufgedeckt.
                    if (surroundingCell.getCellState() == CellState.FREE) {
                        JLabel surroundingLabel = opponent.getLabelForCell(x, y);
                        opponent.markAsMiss(surroundingLabel);
                    }
                }
            }
        }
    }
}