package View;

import model.BoardModel;
import model.CellModel;
import utils.CellState;
import javax.swing.*;
import java.awt.*;

public class BoardView extends JPanel {

    private BoardModel playerBoard;
    private JButton[][] buttons;
    private InfoPanelView infoPanelView;

    public BoardView(BoardModel playerBoard) {
        this.playerBoard = playerBoard;
        this.buttons = new JButton[10][10];  // Initialisiere das Button-Array

        this.setLayout(new BorderLayout());  // Setze Layout des Panels

        // Erstelle Panels für den Namen des Spielers und Informationen
        JPanel playerNamePanel = new JPanel();
        JLabel playerNameLabel = new JLabel("Spielername", SwingConstants.CENTER);
        playerNamePanel.add(playerNameLabel);

        infoPanelView = new InfoPanelView();  // InfoPanel initialisieren

        JPanel gridPanel = new JPanel(new GridLayout(10, 10));  // Erzeuge ein Panel für das Grid
        this.generateBlankBoard(gridPanel);

        JPanel mainPanel = new JPanel(new BorderLayout());  // Erzeuge ein Hauptpanel mit BorderLayout
        mainPanel.add(createLabelsPanel(true), BorderLayout.NORTH);  // Zahlen-Labels oben
        mainPanel.add(createLabelsPanel(false), BorderLayout.WEST);  // Buchstaben-Labels links
        mainPanel.add(gridPanel, BorderLayout.CENTER);  // Grid in der Mitte

        this.add(playerNamePanel, BorderLayout.NORTH);  // Spielername oben
        this.add(mainPanel, BorderLayout.CENTER);  // Hauptpanel in der Mitte
        this.add(infoPanelView, BorderLayout.SOUTH);  // Informationen unten

        this.setVisible(true);
        updateBoard(); // Initialisiere das Spielfeld
    }

    private void generateBlankBoard(JPanel gridPanel) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.buttons[i][j] = createStyledButton();  // Erzeuge einen neuen Button mit Stil
                gridPanel.add(buttons[i][j]);  // Füge den Button zum Grid-Panel hinzu
            }
        }
    }

    private JButton createStyledButton() {
        JButton button = new JButton();
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK); // Ändern Sie die Vordergrundfarbe, damit der Text sichtbar ist
        button.setBorder(BorderFactory.createLineBorder(new Color(0xc5c5ff), 1));
        button.setOpaque(true); // Wichtig, damit die Hintergrundfarbe sichtbar ist
        return button;
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

    public void updateBoard() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (playerBoard.getCell(i, j).getCellState() == CellState.SET) {
                    buttons[i][j].setBackground(Color.GRAY);
                } else {
                    buttons[i][j].setBackground(Color.WHITE);
                }
                buttons[i][j].setOpaque(true); // Wichtig, um sicherzustellen, dass die Hintergrundfarbe sichtbar ist
                buttons[i][j].repaint(); // Erzwingt das Neuzeichnen des Buttons
            }
        }
    }
}
