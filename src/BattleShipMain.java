
import controller.GameController;
import model.GameModel;
import view.HomeScreenView;
import view.GameView;

public class BattleShipMain {
    public static void main(String[] args) {
        GameModel gm = new GameModel();
        HomeScreenView hw = new HomeScreenView();

        GameView gw = new GameView();
        GameController controller = new GameController(gm, gw, hw);
    }
}