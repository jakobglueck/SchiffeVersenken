package controller;

import model.ComputerPlayerModel;
import model.GameModel;
import model.PlayerModel;
import View.GameView;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ShipController {

    private GameModel gameModel;
    private GameView gameView;
    private GameController gameController;

    public ShipController(GameModel gameModel, GameView gameView, GameController gameController) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.gameController = gameController;
    }

    public void handleManualShipPlacement() {
        int shipTurns = (gameModel.getPlayerTwo() instanceof ComputerPlayerModel) ? 1 : 2;

        placeShipsForCurrentPlayer();

        if (shipTurns == 2) {
            gameModel.switchPlayer();
            placeShipsForCurrentPlayer();
        }

        JOptionPane.showMessageDialog(gameView, "Alle Schiffe platziert. Das Spiel beginnt jetzt!");
        gameView.getStatusView().updateStatus("Spiel beginnt!");
        gameController.runGameLoop();
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
                boolean horizontal = SwingUtilities.isRightMouseButton(e);

                if (gameModel.placeNextShip(x, y, horizontal)) {
                    gameController.runGameLoop();
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
}
