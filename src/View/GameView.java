package View;

import model.BoardModel;
import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {

    private BoardView playerBoardOne;
    private BoardView playerBoardTwo;

    public GameView(BoardModel bm) {
        setTitle("Schiffe versenken");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        this.playerBoardOne = new BoardView(bm);
        this.playerBoardTwo = new BoardView(bm);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Labels für Spieler 1 und Spieler 2
        addComponent(new JLabel("Player 1", JLabel.CENTER), gbc, 0, 0, 1, 1, 10, 0.0, 0.0, GridBagConstraints.NONE);
        addComponent(new JLabel("Player 2", JLabel.CENTER), gbc, 2, 0, 1, 1, 10, 0.0, 0.0, GridBagConstraints.NONE);

        // BoardView für Spieler 1 und Spieler 2
        addComponent(playerBoardOne, gbc, 0, 1, 1, 1, 10, 0.5, 0.5, GridBagConstraints.BOTH);
        addComponent(new JPanel(), gbc, 1, 1, 1, 1, 10, 0.0, 0.0, GridBagConstraints.BOTH);
        addComponent(playerBoardTwo, gbc, 2, 1, 1, 1, 10, 0.5, 0.5, GridBagConstraints.BOTH);

        // Unterer Bereich für Bedienelemente
        JPanel controlPanel = new JPanel(new FlowLayout());
        addComponent(controlPanel, gbc, 0, 2, 3, 1, 10, 1.0, 0.0, GridBagConstraints.HORIZONTAL);

        setVisible(true);
    }

    private void addComponent(Component component, GridBagConstraints gbc, int gridx, int gridy, int gridwidth, int gridheight, int insets, double weightx, double weighty, int fill) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.insets = new Insets(insets, insets, insets, insets);
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.fill = fill;
        add(component, gbc);
    }

}
