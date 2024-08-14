
import controller.GameController;
import model.GameModel;
import View.HomeScreenView;
import View.GameView;
import javax.swing.*;
import java.awt.*;

public class SchiffeVersenkenMain {
    public static void main(String[] args) {
        GameModel gm = new GameModel();
        HomeScreenView hw = new HomeScreenView();

        JFrame homeFrame = new JFrame("Schiffe versenken");

        GameView gw = new GameView(gm);
        GameController controller = new GameController(gm, gw, hw, homeFrame);// Übergabe des homeFrame
    }
}