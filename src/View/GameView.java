package View;

import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {
    private BoardView playerBoardOne;
    private BoardView playerBoardTwo;
    public GameView(){
        setTitle("Schiffe versenken");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        setLayout(new GridLayout(2, 2));

        this.playerBoardOne = new BoardView();
        this.playerBoardTwo = new BoardView();

        add(playerBoardOne);
        add(playerBoardTwo);


        this.setVisible(true);
    }
}
