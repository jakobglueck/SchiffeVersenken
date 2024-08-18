package View;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @class GameInfoView
 * @brief Diese Klasse repräsentiert die Statusanzeige im Spiel.
 */
public class GameInfoView extends JPanel {
    /** @brief Label für den Titel der Statusanzeige. */
    private JLabel statusTitleLabel;

    /** @brief Label zur Anzeige des Spielernamens oder Status. */
    private JLabel playerNameLabel;

    /** @brief Label zur Anzeige zusätzlicher Informationen. */
    private JLabel statusMessageLabel;

    /**
     * @brief Konstruktor für die GameInfoView-Klasse.
     * Initialisiert die Komponenten und das Layout.
     */
    public GameInfoView() {
        this.setLayout(new GridLayout(3, 1, 0, -75));

        this.statusTitleLabel = new JLabel("Status", JLabel.CENTER);
        this.playerNameLabel = new JLabel("", JLabel.CENTER);
        this.statusMessageLabel = new JLabel("Warte auf Spielbeginn", JLabel.CENTER);

        this.add(this.statusTitleLabel);
        this.add(this.playerNameLabel);
        this.add(this.statusMessageLabel);

        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * @brief Aktualisiert den Spielernamen oder Status im entsprechenden Label.
     * @param status Der neue Status oder Spielername, der angezeigt werden soll.
     */
    public void updatePlayerName(String status) {
        this.playerNameLabel.setText(status);
    }

    /**
     * @brief Aktualisiert die zusätzlichen Informationen im entsprechenden Label.
     * @param info Die neuen zusätzlichen Informationen, die angezeigt werden sollen.
     */
    public void updateAdditionalInfo(String info) {
        this.statusMessageLabel.setText(info);
    }

    /**
     * @brief Setzt die StatusView auf den Anfangszustand zurück.
     */
    public void reset() {
        this.updatePlayerName("Warten auf Spielbeginn");
        this.updateAdditionalInfo("");
    }
}
