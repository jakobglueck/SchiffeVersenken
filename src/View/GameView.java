package View;

import model.*;
import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {

    private PlayerModel playerOne;
    private PlayerModel playerTwo;
    private BoardView playerBoardOne;
    private BoardView playerBoardTwo;
    private GameModel gameModel;
    private InfoPanelView infoPanelViewOne;
    private InfoPanelView infoPanelViewTwo;
    private StatusView statusView;
    private ControlView controlView;

    public GameView(GameModel gm) {
        this.gameModel = gm;

        setTitle("Schiffe versenken");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setResizable(false);

        this.playerOne = gm.getPlayerOne();
        this.playerTwo = gm.getPlayerTwo();

        this.playerBoardOne = new BoardView(playerOne.getBoard());
        this.playerBoardTwo = new BoardView(playerTwo.getBoard());

        playerBoardOne.updateBoard();
        playerBoardTwo.updateBoard();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        // Zeile 1: PlayerPanel (10% Höhe)
        gbc.weighty = 0.1;
        add(createPlayerPanel(), gbc);

        // Zeile 2: BoardPanel (60% Höhe)
        gbc.weighty = 0.6;
        add(createBoardPanel(), gbc);

        // Zeile 3: InfoPanel/StatusPanel (20% Höhe)
        gbc.weighty = 0.2;
        add(createInfoStatusPanel(), gbc);

        // Zeile 4: ControlPanel (10% Höhe)
        gbc.weighty = 0.1;
        controlView = new ControlView();
        add(controlView, gbc);

        setVisible(true);
    }

    private JPanel createPlayerPanel() {
        JPanel playerPanel = new JPanel(new GridLayout(1, 2));
        PlayerView playerViewOne = new PlayerView(this.playerOne.getPlayerName());
        PlayerView playerViewTwo = new PlayerView(this.playerTwo.getPlayerName());

        playerPanel.add(playerViewOne);
        playerPanel.add(playerViewTwo);

        return playerPanel;
    }

    private JPanel createBoardPanel() {
        JPanel boardPanel = new JPanel(new GridLayout(1, 2));
        boardPanel.add(playerBoardOne);
        boardPanel.add(playerBoardTwo);
        return boardPanel;
    }

    private JPanel createInfoStatusPanel() {
        JPanel infoStatusPanel = new JPanel(new BorderLayout());
        infoPanelViewOne = new InfoPanelView();
        statusView = new StatusView();
        infoPanelViewTwo = new InfoPanelView();

        infoStatusPanel.add(infoPanelViewOne,BorderLayout.WEST);
        infoStatusPanel.add(statusView, BorderLayout.CENTER);
        infoStatusPanel.add(infoPanelViewTwo, BorderLayout.EAST);

        return infoStatusPanel;
    }
    public BoardView getPlayerBoardOne() {
        return this.playerBoardOne;
    }
}