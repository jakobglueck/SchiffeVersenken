package View;

import javax.swing.*;
import java.awt.*;

import model.PlayerModel;
import model.PlayerStatus;

public class InfoPanelView extends JPanel {

    private JLabel totalClicksLabel;
    private JLabel hitsLabel;
    private JLabel sunkShipsLabel;

    public InfoPanelView() {
        setLayout(new GridLayout(4, 1));

        totalClicksLabel = new JLabel();
        hitsLabel = new JLabel();
        sunkShipsLabel = new JLabel();

        add(totalClicksLabel);
        add(hitsLabel);
        add(sunkShipsLabel);

        this.setBorder(BorderFactory.createEmptyBorder(25, 15, 0, 15));
    }

    public void updateStats(PlayerModel playerModel) {
        totalClicksLabel.setText("Anzahl gesamter Klicks: " + playerModel.getPlayerStatus().getTotalClicks());
        hitsLabel.setText("Davon Getroffen (Hits): " + playerModel.getPlayerStatus().getHits());
        sunkShipsLabel.setText("Gegnerische Schiffe versunken: " + playerModel.getPlayerStatus().getShunkShips());
        totalClicksLabel.revalidate();
        totalClicksLabel.repaint();
        hitsLabel.revalidate();
        hitsLabel.repaint();
        sunkShipsLabel.revalidate();
        sunkShipsLabel.repaint();
    }

    public void reset() {
        totalClicksLabel.setText("Anzahl gesamter Klicks: 0");
        hitsLabel.setText("Davon Getroffen (Hits): 0");
        sunkShipsLabel.setText("Gegnerische Schiffe versunken: 0");
        revalidate();
        repaint();
    }

}
