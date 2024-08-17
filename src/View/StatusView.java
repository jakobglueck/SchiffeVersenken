package View;

import javax.swing.*;
import java.awt.*;

public class StatusView extends JPanel {
    private JLabel titleLabel;
    private JLabel playerNameLabel;
    private JLabel additionalInfoLabel;

    public StatusView() {
        this.setLayout(new GridLayout(3, 1, 0, -75));

        titleLabel = new JLabel("Status", SwingConstants.CENTER);
        playerNameLabel = new JLabel("", SwingConstants.CENTER);
        additionalInfoLabel = new JLabel("Warte auf einen Spielzug", SwingConstants.CENTER);

        this.add(titleLabel);
        this.add(playerNameLabel);
        this.add(additionalInfoLabel);

        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public void updatePlayerName(String status) {
        playerNameLabel.setText(status);
        playerNameLabel.revalidate();
        playerNameLabel.repaint();
    }

    public void updateAdditionalInfo(String info) {
        additionalInfoLabel.setText(info);
        additionalInfoLabel.revalidate();
        additionalInfoLabel.repaint();
    }

    public void reset() {
        updatePlayerName("Warten auf Spielbeginn");
        updateAdditionalInfo("");
    }
}
