package View;

import javax.swing.*;
import java.awt.*;

public class StatusView extends JPanel {
    private JLabel titleLabel;
    private JLabel statusLabel;

    public StatusView() {
        this.setLayout(new GridLayout(2, 1,0,-75));

        titleLabel = new JLabel("Status", SwingConstants.CENTER);
        statusLabel = new JLabel("Warten auf Spielbeginn", SwingConstants.CENTER);

        this.add(titleLabel);
        this.add(statusLabel);
        
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public void updateStatus(String status) {
        statusLabel.setText(status);
        statusLabel.revalidate();
        statusLabel.repaint();
    }
}