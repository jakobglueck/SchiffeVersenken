package View;

import model.GameModel;
import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {

    //private GameModel game;
    public GameView(){
        setTitle("Schiffe versenken");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        HomeScreenView hs = new HomeScreenView();
        add(hs, BorderLayout.CENTER);
        setVisible(true);

        /*
        this.playerBoardOne = new BoardView();
        this.playerBoardTwo = new BoardView();

        setLayout(new GridBagLayout());  // Verwende GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();

        // Füge das erste BoardView an Position (0,0) hinzu
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 20, 10); // Abstand hinzufügen
        add(playerBoardOne, gbc);

        // Füge ein leeres Panel an Position (0,1) hinzu
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(new JPanel(), gbc);

        // Füge das zweite BoardView an Position (1,0) hinzu
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(20, 10, 10, 10); // Abstand hinzufügen
        add(playerBoardTwo, gbc);

        // Füge ein leeres Panel an Position (1,1) hinzu
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(new JPanel(), gbc);

        setVisible(true);
       */
    }
}
