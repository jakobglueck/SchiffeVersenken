package View;

import model.BoardModel;
import model.CellModel;
import utils.CellState;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardView extends JPanel {

    private BoardModel playerBoard;
    private JLabel[][] labels;

    private static final int CELL_SIZE = 50;

    public BoardView(BoardModel playerBoard) {
        this.playerBoard = playerBoard;
        this.labels = new JLabel[10][10];

        this.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createLabelsPanel(true), BorderLayout.NORTH);
        mainPanel.add(createLabelsPanel(false), BorderLayout.WEST);
        mainPanel.add(createGridPanel(), BorderLayout.CENTER);

        this.add(mainPanel, BorderLayout.CENTER);

        this.setBorder(BorderFactory.createEmptyBorder(25, 25, 0, 25));

        this.setVisible(true);
        updateBoard();
    }

    private JPanel createLabelsPanel(boolean isNumeric) {
        JPanel labelsPanel = new JPanel(new GridLayout(isNumeric ? 1 : 10, isNumeric ? 10 : 1));
        if (isNumeric) {
            for (int i = 1; i <= 10; i++) {
                JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
                labelsPanel.add(label);
            }
        } else {
            char[] labels = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
            for (char labelChar : labels) {
                JLabel label = new JLabel(String.valueOf(labelChar), SwingConstants.CENTER);
                labelsPanel.add(label);
            }
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
                handleCellClick(row, col, label);
            }
        });
        return label;
    }

    private void handleCellClick(int row, int col, JLabel label) {
        CellModel cell = playerBoard.getCell(row, col);
        if (cell.getCellState() == CellState.FREE) {
            markAsMiss(label);
        } else if (cell.getCellState() == CellState.SET || cell.getCellState() == CellState.HIT) {
            playerBoard.registerHit(row, col);
        }
        updateBoard();
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
            case REVEAL:
                updateRevealedCell(label);
                break;
        }

        label.repaint();
    }

    private void updateFreeCell(JLabel label) {
        if (label.getIcon() == null) {
            label.setIcon(null);
            label.setBackground(Color.WHITE);
        }
    }

    private void updateSetCell(JLabel label) {
        label.setIcon(null);
        label.setBackground(Color.GRAY);
    }

    private void updateHitCell(JLabel label) {
        label.setIcon(IconFactoryView.createCrossIcon(Color.RED, CELL_SIZE / 2));
        label.setBackground(Color.GRAY);
    }

    private void updateRevealedCell(JLabel label) {
        label.setIcon(IconFactoryView.createCrossIcon(Color.RED, CELL_SIZE / 2));
        label.setBackground(Color.RED);
    }
    private void markAsMiss(JLabel label) {
        label.setIcon(IconFactoryView.createPointIcon(Color.BLACK, CELL_SIZE / 4));
    }
}