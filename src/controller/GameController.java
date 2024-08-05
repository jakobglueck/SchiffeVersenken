package controller;

import model.GameModel;
import model.PlayerModel;
import View.GameView;
import utils.GameState;

public class GameController {

    private GameModel gameModel;
    private GameView gameView;

    public GameController(GameModel gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
    }

    public void startGame(GameState gameState) {
        gameModel.setGameState(gameState);
        gameModel.startGame();
        // show gameView

        if (gameState == GameState.NORMAL) {
            this.handleManualShipPlacement();
            // show gameView
        } else if (gameState == GameState.COMPUTER) {
            this.handleManualShipPlacement();
            gameModel.getPlayerTwo().getBoard().placeAllShips();
            // show gameView
        } else if (gameState == GameState.DEBUG) {
            gameModel.getPlayerOne().getBoard().placeAllShips();
            gameModel.getPlayerTwo().getBoard().placeAllShips();
            // show gameView
        }
    }

    private void handleManualShipPlacement() {
        PlayerModel currentPlayer = gameModel.getCurrentPlayer();
        while (!currentPlayer.allShipsPlaced()) {
// show gameView
        }
        // show gameView
    }

    public void handlePlayerMove(int x, int y) {
        if (!gameModel.isGameOver()) {
            gameModel.playerTurn(x, y);
            // show gameView

            if (gameModel.getGameState() == GameState.COMPUTER && !gameModel.isGameOver()) {
                gameModel.computerPlayTurn();
                // show gameView
            }
        }
        if (gameModel.isGameOver()) {
            // show gameView
        }
    }

    public void handleShipPlacement(int startX, int startY, boolean horizontal) {
        if (gameModel.placeNextShip(startX, startY, horizontal)) {
            // show gameView
            if (gameModel.allShipsPlaced()) {
                // show gameView
            } else {
                // show gameView
            }
        } else {
            // show gameView
        }
    }
}
