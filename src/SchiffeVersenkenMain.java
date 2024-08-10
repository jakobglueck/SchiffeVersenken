
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
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homeFrame.setSize(400, 300);
        homeFrame.setContentPane(hw);
        homeFrame.setVisible(true);

        GameView gw = new GameView(gm);
        GameController controller = new GameController(gm, gw, hw, homeFrame); // Übergabe des homeFrame
    }
}