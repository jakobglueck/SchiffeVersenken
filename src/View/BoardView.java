package View;

import model.BoardModel;
import javax.swing.*;
import java.awt.*;

public class BoardView extends JPanel {

    private BoardModel playerBoard;
    private JButton[][] buttons;
    public BoardView(BoardModel playerBoard){
        this.playerBoard = playerBoard;
        this.buttons = new JButton[10][10];  // Initialisiere das Button-Array

        this.setLayout(new GridLayout(10, 10));  // Setze Layout des Panel
        this.generateBlankBoard();
        this.setVisible(true);
    }
    public void generateBlankBoard() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.buttons[i][j] = createStyledButton();  // Erzeuge einen neuen Button mit Stil
                this.add(buttons[i][j]);  // Füge den Button zum Panel hinzu
            }
        }
    }
    private JButton createStyledButton() {
        JButton button = new JButton();
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.MAGENTA);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button.setToolTipText("Tooltip text");
        return button;
    }
}
