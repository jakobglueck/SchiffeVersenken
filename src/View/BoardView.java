package View;

import model.BoardModel;
import model.CellModel;
import model.ShipModel;
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
        mainPanel.add(createNumericLabelsPanel(), BorderLayout.NORTH);
        mainPanel.add(createAlphabeticLabelsPanel(), BorderLayout.WEST);
        mainPanel.add(createGridPanel(), BorderLayout.CENTER);

        this.add(mainPanel, BorderLayout.CENTER);

        this.setBorder(BorderFactory.createEmptyBorder(25, 25, 0, 25));

        this.setVisible(true);
        updateBoard();
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
                handleCellClick(row, col, label);
            }
        });
        return label;
    }

    /**
     * Verarbeitet den Klick auf eine Zelle.
     * @param row Die Zeile der geklickten Zelle.
     * @param col Die Spalte der geklickten Zelle.
     * @param label Das geklickte JLabel.
     */
    private void handleCellClick(int row, int col, JLabel label) {
        CellModel cell = playerBoard.getCell(row, col);

        switch(cell.getCellState()) {
            case FREE:
                this.markAsMiss(label);
                playerBoard.changeCellState(row, col, CellState.FREE);
                break;
            case SET:
                ShipModel model = playerBoard.registerHit(row, col);
                if (model != null) {
                    this.updateHitCell(label);
                    if (model.isSunk()) {
                        this.updateRevealedShip(model); // Aktualisieren Sie alle Labels des versenkten Schiffs
                        this.markSurroundingCellsAsMiss(model); // Markieren Sie alle Zellen um das versenkte Schiff als verfehlt
                    }
                }
                break;
            default:
                System.out.println("Ungültiger Klick.");
        }
        if(this.playerBoard.allShipsAreHit()){
            System.exit(0);
        }
    }

    private void updateRevealedShip(ShipModel ship) {
        for (CellModel cell : ship.getShipCells()) {
            JLabel cellLabel = getLabelForCell(cell.getX(), cell.getY());
            this.updateRevealedCell(cellLabel);
        }
    }

    private JLabel getLabelForCell(int x, int y) {

        return labels[x][y];
    }

    private void markSurroundingCellsAsMiss(ShipModel ship) {
        for (CellModel cell : ship.getShipCells()) {
            int startX = Math.max(0, cell.getX() - 1);
            int endX = Math.min(BoardModel.WIDTH - 1, cell.getX() + 1);
            int startY = Math.max(0, cell.getY() - 1);
            int endY = Math.min(BoardModel.HEIGHT - 1, cell.getY() + 1);

            for (int x = startX; x <= endX; x++) {
                for (int y = startY; y <= endY; y++) {
                    CellModel surroundingCell = playerBoard.getCell(x, y);
                    if (surroundingCell.getCellState() == CellState.FREE) {
                        JLabel surroundingLabel = getLabelForCell(x, y);
                        this.markAsMiss(surroundingLabel);
                        playerBoard.changeCellState(x, y, CellState.FREE);
                    }
                }
            }
        }
    }

    //Diese Methode durchläuft alle Zellen des Spielfelds und aktualisiert jede einzelne Zelle.
    public void updateBoard() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                updateCell(row, col);
            }
        }
    }

    //Aktualisiert eine einzelne Zelle mithilfe der row & col basierend auf ihrem Zustand.
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
                System.exit(0);
        }

    }

    // Markiert die Zelle weiß, um anzuzeigen, dass die Zelle frei ist.
    private void updateFreeCell(JLabel label) {
        label.setBackground(Color.WHITE);
    }

    //Markiert die Zelle grau, um anzuzeigen, dass die Zelle ein Schiff enthält.
    private void updateSetCell(JLabel label) {
        label.setBackground(Color.GRAY);
    }

   //Markiert eine Zelle als getroffen, mithilfe eines roten Kreuz-Icon
    private void updateHitCell(JLabel label) {
        label.setIcon(IconFactoryView.createCrossIcon(Color.RED, CELL_SIZE / 2));
    }

    //Markiert eine Zelle als aufgedeckt, mithilfe eines roten Kreuz-Icon & einer roten Hintergrundfarbe
    private void updateRevealedCell(JLabel label) {
        label.setIcon(IconFactoryView.createCrossIcon(Color.RED, CELL_SIZE / 2));
        label.setBackground(Color.RED);
    }

     // Markiert eine Zelle als verfehlt mithilfe eines schwarzen Punktes.
    private void markAsMiss(JLabel label) {
        label.setIcon(IconFactoryView.createPointIcon(Color.BLACK, CELL_SIZE / 4));
    }
}
