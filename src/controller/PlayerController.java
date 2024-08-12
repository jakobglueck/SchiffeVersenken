package controller;

import model.GameModel;
import View.GameView;

public class PlayerController {

    private GameModel gameModel;
    private GameView gameView;

    public PlayerController(GameModel gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
    }

    public void createPlayers(String playerOneName, String playerTwoName) {
        gameModel.createPlayerWithNames(playerOneName, playerTwoName);
    }

    public void switchPlayer() {
        gameModel.switchPlayer();
    }

    public String getCurrentPlayerName() {
        return gameModel.getCurrentPlayer().getPlayerName();
    }
}
