package view;

import model.*;
import utils.GameState;

import javax.swing.*;
import java.awt.*;

/**
 * @class GameView
 * @brief Diese Klasse ist für die Darstellung der gesamten Spieloberfläche verantwortlich. Dazu gehören:
 * - Anzeige des aktuellen Spielmoduses
 * - Anzeige der Namen der Spieler
 * - Anzeige der Spielfelder für beide Spieler
 * - Anzeige der Spielstatistiken und -informationen
 * - Anzeige der Steuerungselemente des Spiels
 */
public class GameView extends JFrame {

    /**
     * @brief Label zur Anzeige des Spielmodus.
     */
    private JLabel gameModeLabel;

    /**
     * @brief Anzeige des Namens des ersten Spielers.
     */
    private PlayerNameView playerNameViewOne;

    /**
     * @brief Anzeige des Namens des zweiten Spielers.
     */
    private PlayerNameView playerNameViewTwo;

    /**
     * @brief Spielbrett des ersten Spielers.
     */
    private BoardView playerBoardOne;

    /**
     * @brief Spielbrett des zweiten Spielers.
     */
    private BoardView playerBoardTwo;

    /**
     * @brief Anzeige für allgemeine Spielinformationen.
     */
    private GameInfoView gameInfoView;

    /**
     * @brief Statistikansicht des ersten Spielers.
     */
    private StatsView statsViewOne;

    /**
     * @brief Statistikansicht des zweiten Spielers.
     */
    private StatsView statsViewTwo;

    /**
     * @brief Steuerungsansicht für das Spiel.
     */
    private GameControlView gameControlView;

    /**
     * @brief Hauptpanel, das alle anderen Komponenten enthält.
     */
    private JPanel mainPanel;

    /**
     * @brief Konstruktor, der das Hauptfenster des Spiels initialisiert.
     */
    public GameView() {
        initializeGameWindow();
    }

    /**
     * @brief Gibt das Spielfeld des ersten Spielers zurück.
     * @return Das Spielfeld des ersten Spielers
     */
    public BoardView getPlayerBoardOne() {
        return this.playerBoardOne;
    }

    /**
     * @brief Gibt das Spielfeld des zweiten Spielers zurück.
     * @return Das Spielfeld des zweiten Spielers
     */
    public BoardView getPlayerBoardTwo() {
        return this.playerBoardTwo;
    }

    /**
     * @brief Gibt die Statistikanzeige des ersten Spielers zurück.
     * @return Die Statistikanzeige des ersten Spielers
     */
    public StatsView getInfoPanelViewOne() {
        return this.statsViewOne;
    }

    /**
     * @brief Gibt die Statistikanzeige des zweiten Spielers zurück.
     * @return Die Statistikanzeige des zweiten Spielers
     */
    public StatsView getInfoPanelViewTwo() {
        return this.statsViewTwo;
    }

    /**
     * @brief Gibt das Steuerelement-Panel des Spiels zurück.
     * @return Das Steuerelement-Panel des Spiels
     */
    public GameControlView getControlView() {
        return this.gameControlView;
    }

    /**
     * @brief Gibt das Informations- und Statuspanel des Spiels zurück.
     * @return Das Informations- und Statuspanel des Spiels
     */
    public GameInfoView getStatusView() {
        return this.gameInfoView;
    }

    /**
     * @brief Initialisiert das Hauptfenster und das Hauptpanel des Spiels.
     *
     * Legt den Titel, die Größe, sowie weitere Eigenschaften des Fensters fest & fügt das Hauptpanel zum Fenster hinzu.
     */
    private void initializeGameWindow() {
        this.setTitle("Schiffe versenken");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 800);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        this.mainPanel = new JPanel(new GridBagLayout());
        this.add(this.mainPanel);
    }

    /**
     * @brief Konfiguriert die graphische Benutzeroberfläche des Spiels.
     *
     * Diese Methode initialisiert die Spielfelder für beide Spieler und fügt
     * verschiedene Panels zur Hauptanzeige hinzu. Dazu gehören:
     * - Anzeige des aktuellen Spielmoduses
     * - Anzeige der Namen der Spieler
     * - Anzeige der Spielfelder für beide Spieler
     * - Anzeige der Spielstatistiken und -informationen
     * - Anzeige der Steuerungselemente des Spiels
     *
     * @param playerOne Das Model des ersten Spielers, welche das Spielfeld und weitere Informationen enthalten.
     * @param playerTwo Das Model des zweiten Spielers, welche das Spielfeld und weitere Informationen enthalen.
     */
    public void setupGameInterface(PlayerModel playerOne, PlayerModel playerTwo) {

        initializePlayerBoards(playerOne, playerTwo);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        // GameMode Panel
        gbc.weighty = 0.05;
        this.mainPanel.add(this.createGameModePanel(), gbc);

        // Player Panel
        gbc.weighty = 0.05;
        this.mainPanel.add(this.createPlayerNamePanel(playerOne, playerTwo), gbc);

        // Board Panel
        gbc.weighty = 0.65;
        this.mainPanel.add(this.createPlayerBoardPanel(), gbc);

        // Info Panel
        gbc.weighty = 0.10;
        this.mainPanel.add(this.createStatusPanel(), gbc);

        // Game Control Panel
        gbc.weighty = 0.1;
        this.mainPanel.add(this.createGameControlPanel(), gbc);

        this.setVisible(true);
    }

    /**
     * @brief Initialisiert die Spielfelder für beide Spieler.
     *
     * Diese Methode erstellt neue BoardView-Instanzen für beide Spieler
     * und aktualisiert diese mit den aktuellen Spielbrettdaten der jeweiligen Spieler.
     *
     * @param playerOne Das Model des ersten Spielers, welche das Spielfeld und weitere Informationen enthalten.
     * @param playerTwo Das Model des zweiten Spielers, welche das Spielfeld und weitere Informationen enthalen.
     */
    private void initializePlayerBoards(PlayerModel playerOne, PlayerModel playerTwo) {
        this.playerBoardOne = new BoardView();
        this.playerBoardTwo = new BoardView();
        this.playerBoardOne.updateBoard(playerOne.getBoard());
        this.playerBoardTwo.updateBoard(playerTwo.getBoard());
    }

    /**
     * @brief Erstellt ein Panel zur Anzeige der Spielernamen.
     * @param playerOne Das Model des ersten Spielers, welche das Spielfeld und weitere Informationen enthalten.
     * @param playerTwo Das Model des zweiten Spielers, welche das Spielfeld und weitere Informationen enthalen.
     * @return Ein JPanel mit den Spielernamen
     */
    private JPanel createPlayerNamePanel(PlayerModel playerOne, PlayerModel playerTwo) {
        JPanel playerPanel = new JPanel(new GridLayout(1, 2));
        this.playerNameViewOne = new PlayerNameView(playerOne.getPlayerName());
        this.playerNameViewTwo = new PlayerNameView(playerTwo.getPlayerName());

        playerPanel.add(this.playerNameViewOne);
        playerPanel.add(this.playerNameViewTwo);

        return playerPanel;
    }

    /**
     * @brief Erstellt ein Panel zur Darstellung der Spielbretter beider Spieler.
     *
     * Diese Methode erzeugt ein JPanel, das die Spielfelder für beide Spieler nebeneinander
     * in einem GridLayout anordnet und zurückgibt.
     *
     * @return Ein JPanel, das die Spielfelder beider Spieler enthält.
     */
    private JPanel createPlayerBoardPanel() {
        JPanel boardPanel = new JPanel(new GridLayout(1, 2));
        boardPanel.add(this.playerBoardOne);
        boardPanel.add(this.playerBoardTwo);
        return boardPanel;
    }

    /**
     * @brief Erstellt ein Panel zur Anzeige des aktuellen Spielmodus.
     *
     * Diese Methode erzeugt ein JPanel mit einem Label, das den aktuellen Spielmodus anzeigt.
     * Das Label wird mit einer fetten Schriftart formatiert und dem Panel hinzugefügt.
     *
     * @return Ein JPanel, das den aktuellen Spielmodus anzeigt.
     */
    private JPanel createGameModePanel() {
        JPanel gameModePanel = new JPanel(new GridLayout(1, 1));
        this.gameModeLabel = new JLabel("", SwingConstants.CENTER);
        this.gameModeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gameModePanel.add(this.gameModeLabel);

        return gameModePanel;
    }
    /**
     * @brief Erstellt ein Panel zur Anzeige von Spielstatistiken und -informationen.
     *
     * Diese Methode erzeugt ein JPanel, das zwei Statistikansichten und eine Spielinformationsansicht enthält.
     * Die Statistikansichten werden links und rechts, die Spielinformationsansicht zentral im Panel positioniert.
     *
     * @return Ein JPanel, das Spielstatistiken und -informationen anzeigt.
     */
    private JPanel createStatusPanel() {
        JPanel infoStatusPanel = new JPanel(new BorderLayout());
        this.statsViewOne = new StatsView();
        this.gameInfoView = new GameInfoView();
        this.statsViewTwo = new StatsView();

        infoStatusPanel.add(this.statsViewOne, BorderLayout.WEST);
        infoStatusPanel.add(this.gameInfoView, BorderLayout.CENTER);
        infoStatusPanel.add(this.statsViewTwo, BorderLayout.EAST);

        return infoStatusPanel;
    }

    /**
     * @brief Erstellt das Panel zur Steuerung des Spiels.
     *
     * Diese Methode initialisiert die Ansicht für die Spielsteuerung mit den Buttons
     * -"Zurück zum Hauptmenü".
     * -"Pausieren".
     * -"Beenden".
     *
     * @return Ein JPanel, das die Steuerungselemente des Spiels enthält.
     */
    private JPanel createGameControlPanel() {
        this.gameControlView = new GameControlView();
        return this.gameControlView;
    }
    /**
     * @brief Zeigt einen Dialog an, wenn das Spiel beendet ist, und fragt den Spieler, ob ein neues Spiel gestartet werden soll.
     *
     * Diese Methode öffnet einen Dialog, der den Namen des Gewinners anzeigt und den Benutzer fragt,
     * ob er ein neues Spiel starten oder zum Hauptmenü zurückkehren möchte.
     *
     * @param winner Der Name des Spielers, der das Spiel gewonnen hat.
     * @return Ein Integer-Wert, der die vom Benutzer gewählte Option darstellt:
     *         0 für "Neues Spiel" und 1 für "Hauptmenü".
     */
    public int showGameOverDialog(String winner) {
        return JOptionPane.showOptionDialog(
                this,
                "Spiel vorbei! " + winner + " gewinnt!\nMöchtest du ein neues Spiel starten oder zum Hauptmenü zurückkehren?",
                "Spiel beendet",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Neues Spiel", "Hauptmenü"},
                "Neues Spiel"
        );
    }
    /**
     * @brief Aktualisiert das Label zur Anzeige des aktuellen Spielmodus.
     *
     * Diese Methode setzt den angezeigten Text des Spielmodus-Labels auf den übergebenen Spielmodus.
     *
     * @param gameMode Der anzuzeigende Text für den aktuellen Spielmodus.
     */
    public void updateGameModePanel(String gameMode) {
        this.gameModeLabel.setText(gameMode);
    }

    /**
     * @brief Passt die Sichtbarkeit der Spielbretter basierend auf dem aktuellen Spielmodus & dem aktiven Spieler an.
     *
     * Diese Methode aktualisiert die Sichtbarkeit der Spielfelder beider Spieler,
     * abhängig vom aktuellen Spielmodus und dem aktiven Spieler.
     * Je nach Spielzustand werden die Spielfelder unterschiedlich dargestellt & angezeigt:
     * - Im NORMAL-Modus wird nur das Spielfeld des aktuellen Spielers sichtbar gemacht.
     * - Im DEBUG-Modus werden beide Spielfelder vollständig sichtbar.
     * - Im COMPUTER-Modus wird das Spielfeld des Spielers und das Spielfeld des Computers entsprechend angepasst.
     *
     * @param gameModel Das Model des aktuellen Spiels, das den Zustand und die Spielerinformationen enthält.
     * @param gameState Der aktuelle Spielzustand, der angibt, welcher Modus aktiv ist.
     */
    public void updateBoardVisibility(GameModel gameModel, GameState gameState) {
        switch (gameState) {
            case NORMAL:
                if (gameModel.getCurrentPlayer() == gameModel.getPlayerOne()) {
                    this.playerBoardOne.setGridLabelsOpaque(true);
                    this.playerBoardTwo.setGridLabelsOpaque(false);
                } else if (gameModel.getCurrentPlayer() == gameModel.getPlayerTwo()) {
                    this.playerBoardOne.setGridLabelsOpaque(false);
                    this.playerBoardTwo.setGridLabelsOpaque(true);
                }
                break;
            case DEBUG:
                this.playerBoardOne.setGridLabelsOpaque(true);
                this.playerBoardTwo.setGridLabelsOpaque(true);
                break;
            case COMPUTER:
                this.playerBoardOne.setGridLabelsOpaque(true);
                this.playerBoardTwo.setGridLabelsOpaque(false);
                break;
        }
    }

    /**
     * @brief Setzt die Spielansicht zurück und leert alle relevanten Spielfelder und Anzeigen.
     *
     * Diese Methode stellt den Ausgangszustand der Spielansicht wieder her, indem sie:
     * - Beide Spielfelder zurücksetzt.
     * - Die Spielinformationen löscht.
     * - Die Statistikansichten beider Spieler zurücksetzt.
     *
     * Dadurch wird die gesamte Benutzeroberfläche des Spiels auf ihren Anfangszustand zurückgesetzt.
     */
    public void resetView() {
        this.playerBoardOne.resetBoard();
        this.playerBoardTwo.resetBoard();
        this.gameInfoView.reset();
        this.statsViewOne.reset();
        this.statsViewTwo.reset();
    }
}
