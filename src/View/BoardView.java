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
    private InfoPanelView infoPanelView;

    private static final int CELL_SIZE = 50;

    public BoardView(BoardModel playerBoard) {
        this.playerBoard = playerBoard;
        this.labels = new JLabel[10][10];  // Initialisiere das JLabel-Array

        this.setLayout(new BorderLayout());  // Setze Layout des Panels

        infoPanelView = new InfoPanelView();  // InfoPanel initialisieren

        JPanel mainPanel = new JPanel(new BorderLayout());  // Erzeuge ein Hauptpanel mit BorderLayout
        mainPanel.add(createLabelsPanel(true), BorderLayout.NORTH);  // Zahlen-Labels oben
        mainPanel.add(createLabelsPanel(false), BorderLayout.WEST);  // Buchstaben-Labels links
        mainPanel.add(createGridPanel(), BorderLayout.CENTER);  // Grid in der Mitte

        this.add(mainPanel, BorderLayout.CENTER);  // Hauptpanel in der Mitte
        this.add(infoPanelView, BorderLayout.SOUTH);  // Informationen unten

        this.setBorder(BorderFactory.createEmptyBorder(25, 25, 0, 25));

        this.setVisible(true);
        updateBoard(); // Initialisiere das Spielfeld
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
            cell.updateCellState(CellState.SET);
            label.setBackground(Color.GRAY);
        } else if (cell.getCellState() == CellState.SET) {
            cell.updateCellState(CellState.FREE);
            label.setBackground(Color.WHITE);
        }
    }

    public void updateBoard() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                CellModel cell = playerBoard.getCell(i, j);
                if (cell.getCellState() == CellState.SET) {
                    labels[i][j].setBackground(Color.GRAY);
                } else {
                    labels[i][j].setBackground(Color.WHITE);
                }
                labels[i][j].repaint();
            }
        }
    }
}
