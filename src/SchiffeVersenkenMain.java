import View.*;

import model.GameModel;
import utils.*;
import utils.GameState;

public class SchiffeVersenkenMain{
    public static void main(String[] args){

        GameModel gm = new GameModel();
        gm.setGameState(GameState.DEBUG);
        gm.startGame();

        GameView gameView = new GameView(gm);

        //Controller - TODO
    }
}