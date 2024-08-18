package View;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class BoardView extends JPanel {
    private BoardModel playerBoard;
    private JLabel[][] labels;
    private JLabel shipPreviewLabel;
    private BoardClickListener boardClickListener;
    private List<JLabel> graphicsLabels;  // Liste der Labels für die Grafiken

    private static final int CELL_SIZE = 40;
    public static final int BOARD_SIZE = 10;

    private JPanel gridPanel;
    private JPanel mainPanel;

    public BoardView(BoardModel playerBoard) {
        this.playerBoard = playerBoard;
        this.labels = new JLabel[BOARD_SIZE][BOARD_SIZE];
        this.graphicsLabels = new ArrayList<>();  // Initialisiere die Liste

        setLayout(new BorderLayout());

        this.mainPanel = new JPanel(new BorderLayout());
        this.gridPanel = createGridPanel();
        mainPanel.add(gridPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(25, 25, 0, 25));
        setVisible(true);
    }

    public void createLabelForBoard(){
        mainPanel.add(createNumericLabelsPanel(), BorderLayout.NORTH);
        mainPanel.add(createAlphabeticLabelsPanel(), BorderLayout.WEST);
    }

    public void createPanelForShipPlacement() {
        this.gridPanel.removeAll();
        this.gridPanel.setLayout(new BorderLayout());
        this.gridPanel.setPreferredSize((new Dimension(50, 150)));

        JPanel customGridPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGridLines(g); // Raster nur in diesem speziellen Panel zeichnen
            }
        };

        customGridPanel.setOpaque(false);
        shipPreviewLabel = new JLabel();
        shipPreviewLabel.setOpaque(true);
        shipPreviewLabel.setBackground(new Color(0, 0, 255, 128));
        shipPreviewLabel.setVisible(false);
        customGridPanel.add(shipPreviewLabel);

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                JLabel label = createStyledLabel(i, j);
                labels[i][j] = label;
                customGridPanel.add(label);
            }
        }

        this.gridPanel.add(customGridPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void drawGridLines(Graphics g) {
        g.setColor(new Color(200, 200, 200)); // Helles Grau

        for (int i = 0; i <= BOARD_SIZE; i++) {
            int x = i * CELL_SIZE;
            g.drawLine(x, 0, x, BOARD_SIZE * CELL_SIZE);
        }

        for (int i = 0; i <= BOARD_SIZE; i++) {
            int y = i * CELL_SIZE;
            g.drawLine(0, y, BOARD_SIZE * CELL_SIZE, y);
        }
    }

    public void addGraphicsToCells(int startX, int startY, int length, boolean horizontal) {
        for (int i = 0; i < length; i++) {
            int x = horizontal ? startX : startX + i;
            int y = horizontal ? startY + i : startY;

            if (x < BOARD_SIZE && y < BOARD_SIZE) {
                JLabel graphicLabel = new JLabel();
                graphicLabel.setOpaque(true);
                graphicLabel.setBackground(new Color(0, 0, 255, 128));  // Setzen Sie die gewünschte Farbe und Transparenz
                graphicLabel.setBounds(y * CELL_SIZE, x * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                graphicsLabels.add(graphicLabel);
                gridPanel.add(graphicLabel);
            }
        }
        gridPanel.repaint();
    }

    public void removeGraphics() {
        for (JLabel graphicLabel : graphicsLabels) {
            gridPanel.remove(graphicLabel);
        }
        graphicsLabels.clear();
        gridPanel.repaint();
    }

    public void removePanelForShipPlacement(){
        removeGraphics();  // Entferne die Grafiken, wenn das Panel entfernt wird
        gridPanel.removeAll();
        this.mainPanel.remove(gridPanel);
        this.mainPanel.add(createGridPanel());
        this.updateBoard();
        revalidate();
        repaint();
    }

    private JPanel createNumericLabelsPanel() {
        JPanel labelsPanel = new JPanel(new GridLayout(1, 10));
        JLabel tempLabel = new JLabel(String.valueOf(""), SwingConstants.CENTER);
        tempLabel.setPreferredSize(new Dimension(10, 10));
        labelsPanel.add(tempLabel);
        for (int i = 1; i <= 10; i++) {
            JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(20, 20));
            labelsPanel.add(label);
        }
        return labelsPanel;
    }

    private JPanel createAlphabeticLabelsPanel() {
        JPanel labelsPanel = new JPanel(new GridLayout(10, 1));
        char[] labels = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        for (char labelChar : labels) {
            JLabel label = new JLabel(String.valueOf(labelChar), SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
            labelsPanel.add(label);
        }
        return labelsPanel;
    }

    private JPanel createGridPanel() {
        JPanel gridPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                JLabel label = createStyledLabel(i, j);
                labels[i][j] = label;
                gridPanel.add(label);
            }
        }
        return gridPanel;
    }

    private JLabel createStyledLabel(int row, int col) {
        JLabel label = new JLabel();
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setBorder(BorderFactory.createLineBorder(new Color(0xc5c5ff), 1));
        label.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (boardClickListener != null) {
                    boardClickListener.onCellClicked(row, col, label);
                }
            }
        });

        return label;
    }

    public void setGridLabelsOpaque(boolean opaque) {
        for (JLabel[] labelRow : labels) {
            for (JLabel jLabel : labelRow) {
                jLabel.setOpaque(opaque);
                jLabel.repaint();
            }
        }
    }

    public void toggleGridVisibility(boolean visible) {
        gridPanel.setVisible(visible);

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                labels[i][j].setVisible(!visible);
            }
        }
        revalidate();
        repaint();
    }

    public void updateShipPreview(int x, int y, int width, int height) {
        shipPreviewLabel.setBounds(x, y, width, height);
        shipPreviewLabel.setVisible(true);
        gridPanel.repaint();
    }

    public void hideShipPreview() {
        shipPreviewLabel.setVisible(false);
        gridPanel.repaint();
    }

    public void setBoardClickListener(BoardClickListener listener) {
        this.boardClickListener = listener;
    }

    public void updateBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                updateCell(row, col);
            }
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void updateCell(int row, int col) {
        CellModel cell = playerBoard.getCell(row, col);
        JLabel label = labels[row][col];

        switch (cell.getCellState()) {
            case FREE:
                updateFreeCell(label);
                break;
            case SET:
                updateSetCell(label);
                break;
            case HIT:
                updateHitCell(label);
                break;
            default:
                label.setBackground(Color.WHITE);
        }
        label.revalidate();
        label.repaint();
    }

    private void updateFreeCell(JLabel label) {
        label.setBackground(Color.WHITE);
    }

    private void updateSetCell(JLabel label) {
        label.setBackground(Color.BLUE);
    }

    private void updateHitCell(JLabel label) {
        label.setIcon(IconFactoryView.createCrossIcon(Color.RED, CELL_SIZE / 4));
    }

    public void markAsMiss(JLabel label) {
        label.setIcon(IconFactoryView.createPointIcon(Color.BLACK, CELL_SIZE / 4));
    }

    public void updateRevealedShip(ShipModel ship) {
        for (CellModel cell : ship.getShipCells()) {
            JLabel cellLabel = getLabelForCell(cell.getX(), cell.getY());
            cellLabel.setIcon(IconFactoryView.createCrossIcon(Color.RED, CELL_SIZE / 2));
            cellLabel.setBackground(Color.RED);
        }
    }

    public JLabel getLabelForCell(int row, int col) {
        return labels[row][col];
    }

    public interface BoardClickListener {
        void onCellClicked(int row, int col, JLabel label);
    }

    public BoardModel getPlayerBoard() {
        return playerBoard;
    }

    private void clearLabelGraphics(JLabel label) {
        label.setIcon(null);
        label.setBackground(Color.WHITE);
        label.setBorder(BorderFactory.createLineBorder(new Color(0xc5c5ff), 1));
        label.revalidate();
        label.repaint();
    }

    public void resetBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                clearLabelGraphics(labels[row][col]);
            }
        }
        revalidate();
        repaint();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        setFocusable(true);
        requestFocus();
    }

    public int getCellSize() {
        return CELL_SIZE;
    }
}
