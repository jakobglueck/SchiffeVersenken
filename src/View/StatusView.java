package View;

import javax.swing.*;
import java.awt.*;

public class StatusView extends JPanel {
    private JLabel statusLabel;

    public StatusView() {
        this.setLayout(new BorderLayout());
        statusLabel = new JLabel("Status: Warten auf Spielbeginn", SwingConstants.CENTER);
        this.add(statusLabel, BorderLayout.CENTER);

        // Setze Padding
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public void updateStatus(String status) {
        statusLabel.setText("Status: " + status);
    }
}
