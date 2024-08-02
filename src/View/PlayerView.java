package View;

import javax.swing.*;
import java.awt.*;

public class PlayerView extends JPanel {
    private JLabel playerNameLabel;

    public PlayerView(String playerName) {
        this.setLayout(new BorderLayout());
        playerNameLabel = new JLabel(playerName, SwingConstants.CENTER);
        this.add(playerNameLabel, BorderLayout.CENTER);
    }

    public void setPlayerName(String playerName) {
        playerNameLabel.setText(playerName);
    }
}
