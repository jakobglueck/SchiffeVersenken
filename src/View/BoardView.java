package View;

import model.BoardModel;
import model.CellModel;
import model.ShipModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BoardView extends JPanel {
    private BoardModel playerBoard;
    private JLabel[][] labels;
    private JLabel shipPreviewLabel;
    private BoardClickListener boardClickListener;

    private static final int CELL_SIZE = 40;
    public static final int BOARD_SIZE = 10;

    private JPanel gridPanel;
    private JPanel mainPanel;

    public BoardView(BoardModel playerBoard) {
        this.playerBoard = playerBoard;
        this.labels = new JLabel[BOARD_SIZE][BOARD_SIZE];

        setLayout(new BorderLayout());

        this.mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createNumericLabelsPanel(), BorderLayout.NORTH);
        mainPanel.add(createAlphabeticLabelsPanel(), BorderLayout.WEST);

        this.gridPanel = createGridPanel();
        mainPanel.add(gridPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(25, 25, 0, 25));
        setVisible(true);
    }

    public void createPanelForShipPlacement(){
        this.gridPanel.removeAll(); // Entferne vorhandene Komponenten, falls nötig
        this.gridPanel.setLayout(null); // Enable absolute positioning

        shipPreviewLabel = new JLabel();
        shipPreviewLabel.setOpaque(true);
        shipPreviewLabel.setBackground(new Color(0, 0, 255, 128)); // Halbtransparentes Blau
        shipPreviewLabel.setVisible(false); // Initially hidden
        gridPanel.add(shipPreviewLabel);

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                JLabel label = createStyledLabel(i, j);
                labels[i][j] = label;
                gridPanel.add(label);
            }
        }

        revalidate();
        repaint();
    }

    public void removePanelForShipPlacement(){
        gridPanel.removeAll();
        this.mainPanel.remove(gridPanel);
        this.mainPanel.add(createGridPanel());
        this.updateBoard();
        revalidate();
        repaint();

    }

    private JPanel createNumericLabelsPanel() {
        JPanel labelsPanel = new JPanel(new GridLayout(1, 10)); // 1 Zeile, 10 Spalten
        JLabel tempLabel = new JLabel(String.valueOf(""), SwingConstants.CENTER);
        tempLabel.setPreferredSize(new Dimension(10, 10));
        labelsPanel.add(tempLabel);
        for (int i = 1; i <= 10; i++) {
            JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER); // Zentrierte Ausrichtung
            label.setPreferredSize(new Dimension(20, 20)); // Feste Größe für jedes Label
            labelsPanel.add(label);
        }

        return labelsPanel;
    }

    private JPanel createAlphabeticLabelsPanel() {
        JPanel labelsPanel = new JPanel(new GridLayout(10, 1)); // 10 Zeilen, 1 Spalte

        char[] labels = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        for (char labelChar : labels) {
            JLabel label = new JLabel(String.valueOf(labelChar), SwingConstants.CENTER); // Zentrierte Ausrichtung
            label.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE)); // Feste Größe für jedes Label
            labelsPanel.add(label);
        }

        return labelsPanel;
    }

    private JPanel createGridPanel() {
        JPanel gridPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE)); // GridLayout für das Spielfeld

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                JLabel label = createStyledLabel(i, j);  // Erzeugt ein Label für jede Zelle
                labels[i][j] = label;  // Speichert das Label im Array
                gridPanel.add(label);  // Fügt das Label dem Grid-Panel hinzu
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

        // Setze die Sichtbarkeit der Labels
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                labels[i][j].setVisible(!visible); // Labels sollten sichtbar sein, wenn das Grid eingeblendet ist
            }
        }
        revalidate();
        repaint();
    }

    public void updateShipPreview(int x, int y, int width, int height) {
        // Set the bounds of the ship preview label relative to the gridPanel
        shipPreviewLabel.setBounds(x, y, width, height);
        shipPreviewLabel.setVisible(true);
        gridPanel.repaint(); // Repaint the gridPanel, not the entire BoardView
    }

    public void hideShipPreview() {
        shipPreviewLabel.setVisible(false);
        gridPanel.repaint(); // Repaint the gridPanel, not the entire BoardView
    }

    public void setBoardClickListener(BoardClickListener listener) {
        this.boardClickListener = listener;
    }

    public MouseListener getBoardClickListener() {
        return (MouseListener) this.boardClickListener;
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
        label.setBackground(Color.BLUE);  // Set the cell to blue when a ship is placed
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

    public void addBoardMouseListener(MouseListener listener) {
        addMouseListener(listener);
    }

    public void removeBoardMouseListener(MouseListener listener) {
        removeMouseListener(listener);
    }

    public void addBoardMouseMotionListener(MouseMotionListener listener) {
        addMouseMotionListener(listener);
    }

    public void removeBoardMouseMotionListener(MouseMotionListener listener) {
        removeMouseMotionListener(listener);
    }

    public void addBoardKeyListener(KeyListener listener) {
        addKeyListener(listener);
    }

    public void removeBoardKeyListener(KeyListener listener) {
        removeKeyListener(listener);
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

    public JLabel getShipPreviewLabel() {
        return shipPreviewLabel;
    }
}
