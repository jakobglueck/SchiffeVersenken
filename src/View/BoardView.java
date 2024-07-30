package View;

import javax.swing.*;
import java.awt.*;

public class BoardView extends JPanel {
    public BoardView(){
        JLabel label = new JLabel("Hallo");
        this.add(label);
        this.setVisible(true);
        generateBoard();
    }
    public void generateBoard() {
        this.setLayout(new GridLayout(10, 10));
        //TODO
    }
}
