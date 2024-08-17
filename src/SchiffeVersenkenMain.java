
import controller.GameController;
import model.GameModel;
import View.HomeScreenView;
import View.GameView;

public class SchiffeVersenkenMain {
    public static void main(String[] args) {
        GameModel gm = new GameModel();
        HomeScreenView hw = new HomeScreenView();

        GameView gw = new GameView(gm);
        GameController controller = new GameController(gm, gw, hw);
    }
}