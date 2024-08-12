package View;

import javax.swing.*;
import java.awt.*;

import model.PlayerModel;
import model.PlayerStatus;

public class InfoPanelView extends JPanel {

    private JLabel totalClicksLabel;
    private JLabel hitsLabel;
    private JLabel missesLabel;
    private JLabel sunkShipsLabel;

    public InfoPanelView() {
        setLayout(new GridLayout(4, 1));

        totalClicksLabel = new JLabel();
        hitsLabel = new JLabel();
        missesLabel = new JLabel();
        sunkShipsLabel = new JLabel();

        add(totalClicksLabel);
        add(hitsLabel);
        add(missesLabel);
        add(sunkShipsLabel);

        this.setBorder(BorderFactory.createEmptyBorder(25, 15, 0, 15));
    }

    public void updateStats(PlayerModel playerModel) {
        totalClicksLabel.setText("Anzahl gesamter Klicks: " + playerModel.getPlayerStatus().getTotalClicks());
        hitsLabel.setText("Davon Getroffen (Hits): " + playerModel.getPlayerStatus().getHits());
        missesLabel.setText("Verfehlt: " + playerModel.getPlayerStatus().getMisses());
        sunkShipsLabel.setText("Gegnerische Schiffe versunken: " + playerModel.getPlayerStatus().getShunkShips());
    }
    public JLabel getTotalClicksLabel() {
        return totalClicksLabel;
    }

    public JLabel getHitsLabel() {
        return hitsLabel;
    }

    public JLabel getMissesLabel() {
        return missesLabel;
    }

    public JLabel getSunkShipsLabel() {
        return sunkShipsLabel;
    }
}
