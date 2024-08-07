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

        // Hinzufügen eines Maus-Listeners, der auf Mausklicks reagiert
        label.addMouseListener(new MouseAdapter() {
            //Beim klicken wird diese Methode aufgerufen
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
        // Holt die Zelle im angegebenen (row, col) vom playerBoard, welche angeklickt wurde
        CellModel cell = playerBoard.getCell(row, col);

        if (cell.getCellState() == CellState.FREE) {
            markAsMiss(label);
        } else if (cell.getCellState() == CellState.SET || cell.getCellState() == CellState.HIT) {
            playerBoard.registerHit(row, col);
        }
        updateBoard();
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
            case REVEAL:
                updateRevealedCell(label);
                break;
            default: System.exit(0);
        }

        label.repaint();
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
        label.setBackground(Color.GRAY);
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
