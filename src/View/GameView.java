package View;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameView extends JFrame {

    private PlayerModel playerOne;
    private PlayerModel playerTwo;
    private BoardView playerBoardOne;
    private BoardView playerBoardTwo;
    private GameModel gameModel;

    public GameView(GameModel gm) {
        this.gameModel = gm;

        setTitle("Schiffe versenken");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        this.playerOne = gm.getPlayerOne();
        this.playerTwo = gm.getPlayerTwo();

        this.playerBoardOne = new BoardView(playerOne.getBoard());
        this.playerBoardTwo = new BoardView(playerTwo.getBoard());

        playerBoardOne.updateBoard(); // Aktualisiere das Spielbrett

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // PlayerNamePanels für Spieler 1 und Spieler 2
        JPanel playerNamePanelOne = createPlayerNamePanel(this.playerOne.getPlayerName());
        JPanel playerNamePanelTwo = createPlayerNamePanel(this.playerTwo.getPlayerName());

        addComponent(playerNamePanelOne, gbc, 0, 0, 1, 1, 10, 0.0, 0.0, GridBagConstraints.NONE);
        addComponent(playerNamePanelTwo, gbc, 2, 0, 1, 1, 10, 0.0, 0.0, GridBagConstraints.NONE);

        // BoardView für Spieler 1 und Spieler 2
        addComponent(playerBoardOne, gbc, 0, 1, 1, 1, 10, 0.5, 0.5, GridBagConstraints.BOTH);
        addComponent(new JPanel(), gbc, 1, 1, 1, 1, 10, 0.0, 0.0, GridBagConstraints.BOTH);
        addComponent(playerBoardTwo, gbc, 2, 1, 1, 1, 10, 0.5, 0.5, GridBagConstraints.BOTH);

        // Unterer Bereich für Bedienelemente
        JPanel controlPanel = new JPanel(new FlowLayout());
        addControlButtons(controlPanel);
        addComponent(controlPanel, gbc, 0, 2, 3, 1, 10, 1.0, 0.0, GridBagConstraints.HORIZONTAL);

        setVisible(true);
    }

    private JPanel createPlayerNamePanel(String playerName) {
        JPanel playerNamePanel = new JPanel();
        JLabel playerNameLabel = new JLabel(playerName, SwingConstants.CENTER);
        playerNamePanel.add(playerNameLabel);
        return playerNamePanel;
    }

    private void addControlButtons(JPanel controlPanel) {
        JButton mainMenuButton = new JButton("Zurück zum Hauptmenü");
        JButton pauseGameButton = new JButton("Spiel pausieren");
        JButton endGameButton = new JButton("Spiel verlassen");

        endGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        pauseGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Spiel ist pausiert!");
            }
        });

        controlPanel.add(mainMenuButton);
        controlPanel.add(pauseGameButton);
        controlPanel.add(endGameButton);
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
