package View;

import javax.swing.*;
import java.awt.*;

import model.PlayerModel;

/**
 * @class StatsView
 * @brief Diese Klasse stellt eine Ansicht zur Anzeige der Statistiken eines Spielers bereit, einschließlich der Anzahl der Klicks, Treffer und versenkten Schiffe.
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
     * @brief Konstruktor, der die Statistikanzeige initialisiert und die Layouts setzt.
     */
    public StatsView() {
        this.setLayout(new GridLayout(4, 1));

        this.totalClicksLabel = new JLabel();
        this.hitsLabel = new JLabel();
        this.sunkShipsLabel = new JLabel();

        this.add(this.totalClicksLabel);
        this.add(this.hitsLabel);
        this.add(this.sunkShipsLabel);

        this.setBorder(BorderFactory.createEmptyBorder(25, 15, 0, 15));
    }

    /**
     * @brief Aktualisiert die angezeigten Statistiken basierend auf dem übergebenen PlayerModel.
     *
     * @param playerModel Das PlayerModel, das die aktuellen Statistiken des Spielers enthält.
     */
    public void updateStats(PlayerModel playerModel) {
        this.totalClicksLabel.setText("Anzahl gesamter Klicks: " + playerModel.getPlayerStatus().getTotalClicks());
        this.hitsLabel.setText("Davon Getroffen (Hits): " + playerModel.getPlayerStatus().getHits());
        this.sunkShipsLabel.setText("Gegnerische Schiffe versunken: " + playerModel.getPlayerStatus().getSunkShips());

        this.totalClicksLabel.revalidate();
        this.totalClicksLabel.repaint();
        this.hitsLabel.revalidate();
        this.hitsLabel.repaint();
        this.sunkShipsLabel.revalidate();
        this.sunkShipsLabel.repaint();
    }

    /**
     * @brief Setzt die angezeigten Statistiken auf die Anfangswerte zurück.
     */
    public void reset() {
        this.totalClicksLabel.setText("Anzahl gesamter Klicks: 0");
        this.hitsLabel.setText("Davon Getroffen (Hits): 0");
        this.sunkShipsLabel.setText("Gegnerische Schiffe versunken: 0");

        this.revalidate();
        this.repaint();
    }
}
