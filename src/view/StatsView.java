package view;

import javax.swing.*;
import java.awt.*;

import model.PlayerModel;

/**
 * @class StatsView
 * @brief Diese Klasse stellt eine Ansicht zur Anzeige bestimmter Statistiken eines Spielers bereit.
 * Darunter zählen: die Anzahl der Klicks, die Treffer(Hits) und die Anzahl der versenkten Schiffe.
 *
 */
public class StatsView extends JPanel {

    /**
     * @brief Label zur Anzeige der Gesamtzahl der Klicks.
     */
    private JLabel totalClicksLabel;

    /**
     * @brief Label zur Anzeige der Anzahl der Treffer (Hits).
     */
    private JLabel hitsLabel;

    /**
     * @brief Label zur Anzeige der Anzahl der versenkten gegnerischen Schiffe.
     */
    private JLabel sunkShipsLabel;

    /**
     * @brief Konstruktor, der alle Statistikanzeigen initialisiert und die Layouts setzt.
     */
    public StatsView() {
        this.setLayout(new GridLayout(3, 1));

        this.totalClicksLabel = new JLabel();
        this.hitsLabel = new JLabel();
        this.sunkShipsLabel = new JLabel();

        this.add(this.totalClicksLabel);
        this.add(this.hitsLabel);
        this.add(this.sunkShipsLabel);

        this.setBorder(BorderFactory.createEmptyBorder(25, 15, 0, 15));
    }

    /**
     * @brief aktualisiert die angezeigten Statistiken basierend auf dem übergebenen PlayerModel, welcher die Daten enthält.
     *
     * @param playerModel Das PlayerModel, das die Daten, also die aktuellen Statistiken des Spielers enthält.
     */
    public void updateStats(PlayerModel playerModel) {
        this.totalClicksLabel.setText("Anzahl gesamter Klicks: " + playerModel.getPlayerStatus().getTotalClicks());
        this.hitsLabel.setText("Davon Getroffen (Hits): " + playerModel.getPlayerStatus().getHits());
        this.sunkShipsLabel.setText("Gegnerische Schiffe versunken: " + playerModel.getPlayerStatus().getSunkShips());
    }
}
