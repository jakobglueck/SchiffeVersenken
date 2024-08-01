package View;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class IconFactoryView {

    public static Icon createPointIcon(Color color, int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(color);
        g2d.fillOval(0, 0, size, size);
        g2d.dispose();
        return new ImageIcon(image);
    }

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
