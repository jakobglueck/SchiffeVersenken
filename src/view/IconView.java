package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @class IconView
 * @brief Diese Klasse stellt Methoden zur Verfügung, um spezielle Icons zu generieren,
 * die zur Visualisierung von Spielereignissen genutzt werden, wie z.B. Punkte für verfehlte Schüsse
 * und Kreuze für Treffer.
 */

public class IconView {

    /**
     * @brief Erzeugt ein Icon, das einen Punkt darstellt.
     *
     * @param color Die Farbe des Punktes.
     * @param size  Die Größe des Icons, mit Breite und Höhe.
     * @return Ein Icon, das einen Punkt darstellt.
     */
    public static Icon createPointIcon(Color color, int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(color);
        g2d.fillOval(0, 0, size, size);
        g2d.dispose();
        return new ImageIcon(image);
    }

    /**
     * @brief Erzeugt ein Icon, das ein Kreuz darstellt.
     *
     * @param color Die Farbe des Kreuzes.
     * @param size  Die Größe des Icons mit Breite und Höhe.
     * @return Ein Icon, das ein Kreuz darstellt.
     */
    public static Icon createCrossIcon(Color color, int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(0, 0, size, size);
        g2d.drawLine(size, 0, 0, size);
        g2d.dispose();
        return new ImageIcon(image);
    }
}
