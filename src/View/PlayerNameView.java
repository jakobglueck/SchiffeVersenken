package View;

import javax.swing.*;
import java.awt.*;

/**
 * @class PlayerNameView
 * @brief Diese Klasse stellt eine Ansicht zur Anzeige des Spielernamens bereit.
 */
public class PlayerNameView extends JPanel {

    /**
     * @brief Label zur Anzeige des Namens des Spielers.
     */
    private JLabel playerNameLabel;

    /**
     * @brief Konstruktor, der die Ansicht mit dem übergebenen Spielernamen initialisiert.
     *
     * @param playerName Der Name des Spielers, der angezeigt werden soll.
     */
    public PlayerNameView(String playerName) {
        this.setLayout(new BorderLayout());
        this.playerNameLabel = new JLabel(playerName, SwingConstants.CENTER);
        this.add(this.playerNameLabel, BorderLayout.CENTER);

        this.setBorder(BorderFactory.createEmptyBorder(25, 10, 0, 10));
        this.setVisible(true);
    }
}
