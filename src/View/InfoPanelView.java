package View;

import javax.swing.*;
import java.awt.*;

public class InfoPanelView extends JPanel {

    private JLabel totalClicksLabel;
    private JLabel hitsLabel;
    private JLabel missesLabel;
    private JLabel sunkShipsLabel;

    public InfoPanelView() {
        setLayout(new GridLayout(4, 1));

        totalClicksLabel = new JLabel("Anzahl gesamter Klicks: 0");
        hitsLabel = new JLabel("Davon Getroffen (Hits): 0");
        missesLabel = new JLabel("Verfehlt: 0");
        sunkShipsLabel = new JLabel("Gegnerische Schiffe versunken: 0");

        add(totalClicksLabel);
        add(hitsLabel);
        add(missesLabel);
        add(sunkShipsLabel);

        this.setBorder(BorderFactory.createEmptyBorder(25, 15, 0, 15));
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
