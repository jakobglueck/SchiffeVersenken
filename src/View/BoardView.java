package View;

import model.BoardModel;
import model.CellModel;
import model.ShipModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardView extends JPanel {
    private BoardModel playerBoard;
    private JLabel[][] labels;
    private JPanel coverPanel;
    private BoardClickListener boardClickListener;

    private static final int CELL_SIZE = 50;
    private static final int BOARD_SIZE = 10;

    public BoardView(BoardModel playerBoard) {
        this.playerBoard = playerBoard;
        this.labels = new JLabel[BOARD_SIZE][BOARD_SIZE];

        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createNumericLabelsPanel(), BorderLayout.NORTH);
        mainPanel.add(createAlphabeticLabelsPanel(), BorderLayout.WEST);
        mainPanel.add(createGridPanel(), BorderLayout.CENTER);

        this.coverPanel = new JPanel();
        this.coverPanel.setBackground(Color.GRAY);
        this.coverPanel.setOpaque(false);
        this.coverPanel.setVisible(false);

        setLayout(new OverlayLayout(this));
        add(mainPanel);
        add(coverPanel);

        setBorder(BorderFactory.createEmptyBorder(25, 25, 0, 25));
        setVisible(true);
    }

    // Methode zum Steuern der Opazität des coverPanels
    public void setOpaqueCover(boolean opaque) {
        this.coverPanel.setOpaque(opaque);
        this.repaint();
    }

    private JPanel createNumericLabelsPanel() {
        JPanel labelsPanel = new JPanel(new GridLayout(1, 10));

        for (int i = 1; i <= 10; i++) {
            JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            labelsPanel.add(label);
        }

        return labelsPanel;
    }

    private JPanel createAlphabeticLabelsPanel() {
        JPanel labelsPanel = new JPanel(new GridLayout(10, 1));

        char[] labels = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        for (char labelChar : labels) {
            JLabel label = new JLabel(String.valueOf(labelChar), SwingConstants.CENTER);
            labelsPanel.add(label);
        }

        return labelsPanel;
    }

    private JPanel createGridPanel() {
        JPanel gridPanel = new JPanel(new GridLayout(10, 10));
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
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

    public void setBoardClickListener(BoardClickListener listener) {
        this.boardClickListener = listener;
    }

    public void updateBoard() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                updateCell(row, col);
            }
        }
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
    }

    private void updateFreeCell(JLabel label) {
        label.setBackground(Color.WHITE);
    }

    private void updateSetCell(JLabel label) {
        label.setBackground(Color.GRAY);
    }

    private void updateHitCell(JLabel label) {
        label.setIcon(IconFactoryView.createCrossIcon(Color.RED, CELL_SIZE / 2));
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

    public void setCovered(boolean covered) {
        this.coverPanel.setVisible(covered);
    }

    public int getCellSize() {
        return CELL_SIZE;
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
}
