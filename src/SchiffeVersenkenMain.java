import View.*;
import model.BoardModel;
import model.GameModel;

public class SchiffeVersenkenMain{
    public static void main(String[] args){

        //Model - TODO
        BoardModel bm = new BoardModel();
        //View

        GameModel gm = new GameModel();

        GameView gameView = new GameView(bm);

        //Controller - TODO

    }
}

