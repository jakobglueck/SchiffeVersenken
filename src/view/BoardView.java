package view;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @class BoardView
 * @brief Diese Klasse stellt das Spielbrett grafisch dar und ermöglicht die Interaktion mit den Zellen.
 *
 * - Stellt das Spielbrett grafisch mit numerischen und alphabetischen Beschriftungen dar.
 * - Ermöglicht die Platzierung von Schiffen.
 * - Markiert an den Zellen die Treffer und Fehlschüsse.
 * - Erfasst Klicks auf den Zellen des Spielbretts.
 * - Aktualisiert den angezeigten Spielzustand.
 * - Bietet Funktionen zum Zurücksetzen des Spielbretts oder von graphischen Elementen.
 */
public class BoardView extends JPanel {
    /** @brief 2D-Array von JLabels zur Darstellung der Zellen auf dem Spielbrett. */
    private JLabel[][] labels;

    /** @brief dient zur Anzeige einer visuellen Vorschau, bevor ein Schiff auf dem Spielbrett platziert wird. */
    private JLabel shipPreviewLabel;

    /**
     * @brief Listener, für die Erfassung und Verarbeitung von Benutzerklicks auf den Zellen des Spielbretts.
     */
    private BoardClickListener boardClickListener;

    /**
     * @brief Speichert temporäre grafische Elemente(Labels), die auf dem Spielbrett hinzugefügt und
     * später wieder entfernt werden sollen.
     */
    private List<JLabel> graphicsLabels;

    /**
     * @brief Bestimmt die Größe jeder Zelle auf dem Spielbrett in Pixeln.
     */
    private static final int CELL_SIZE = 40;

    /**
     * @brief Definiert die Anzahl der Reihen und Spalten auf dem Spielbrett.
     */
    public static final int BOARD_SIZE = 10;

    /**
     * @brief Panel, das die Zellen des Spielbretts in einem Grid-Layout organisiert.
     */
    private JPanel gridPanel;

    /**
     * @brief Das Hauptpanel, dass das gesamte Spielbrett und zugehörige Komponenten zusammenfasst.
     */
    private JPanel mainPanel;

    /**
     * @brief Konstruktor, der die Spielbrett-Komponenten initialisiert und konfiguriert.
     *
     * Ruft interne Methoden auf, um die Komponenten zu initialisieren, das Hauptpanel zu konfigurieren
     * und das Gesamtlayout des Spielbretts festzulegen.
     */
    public BoardView() {
        initializeComponents();
        configureMainPanel();
        configureBoardView();
    }

    /**
     * @brief Gibt das JLabel für eine bestimmte Zelle im Spielbrett zurück.
     *
     * Diese Methode liefert ein bestimmtes `JLabel`, das die Zelle an der angegebenen
     * Position (Zeile und Spalte) im Spielbrett darstellt.
     *
     * @param row Die Zeile der Zelle.
     * @param col Die Spalte der Zelle.
     * @return Das `JLabel`, das die Zelle an der angegebenen Position darstellt.
     */
    public JLabel getLabelForCell(int row, int col) {
        return labels[row][col];
    }

    /**
     * @brief Gibt die Größe einer Zelle im Spielbrett zurück.
     *
     * Diese Methode liefert die festgelegte Größe einer Zelle im Spielbrett in Pixeln.
     *
     * @return Die Größe einer Zelle in Pixeln.
     */
    public int getCellSize() {
        return CELL_SIZE;
    }


    /**
     * @brief Setzt den Listener für Klickereignisse auf dem Spielbrett.
     *
     * Registriert einen `BoardClickListener`, der auf Klicks auf die Zellen des Spielbretts reagiert.
     *
     * @param listener Ein Objekt, das das `BoardClickListener`-Interface implementiert.
     */
    public void setBoardClickListener(BoardClickListener listener) {
        this.boardClickListener = listener;
    }


    /**
     * @brief Setzt die Opazität aller Zellen im Spielfeld.
     *
     * Diese Methode ändert die Durchsichtigkeit aller Zellen (JLabel) auf dem Spielbrett.
     * Wenn die Opazität auf `true` gesetzt ist, sind die Zellen undurchsichtig.
     * Wenn die Opazität auf `false` gesetzt ist, werden die Zellen durchsichtig.
     *
     * @param opaque Boolean-Wert, der die Opazität steuert. `true` bedeutet undurchsichtig, `false` bedeutet durchsichtig.
     */
    public void setGridLabelsOpaque(boolean opaque) {
        for (JLabel[] labelRow : labels) {
            for (JLabel jLabel : labelRow) {
                jLabel.setOpaque(opaque);
                jLabel.repaint();
            }
        }
    }

    /**
     * @brief Initialisiert die grundlegenden Komponenten des Spielbretts.
     *
     * - Erstellt das 2D-Array für die Zellen des Spielbretts.
     * - Initialisiert die Liste für die temporären, grafischen Elemente.
     */
    private void initializeComponents() {
        this.labels = new JLabel[BOARD_SIZE][BOARD_SIZE];
        this.graphicsLabels = new ArrayList<>();
    }

    /**
     * @brief Konfiguriert das Hauptpanel, das die anderen Komponenten enthält.
     *
     * - Setzt das Layout des Hauptpanels auf BorderLayout.
     * - Initialisiert das Gitterpanel.
     * - Fügt das Gitter(Spielbrett) und die Beschriftungen(Numerische und Alphabetische) zum Hauptpanel hinzu.
     */
    private void configureMainPanel() {
        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.setPreferredSize(new Dimension(50, 189));
        this.gridPanel = createGridPanel();
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(createNumericLabelsPanel(), BorderLayout.NORTH);
        mainPanel.add(createAlphabeticLabelsPanel(), BorderLayout.WEST);
    }

    /**
     * @brief Konfiguriert das BoardView-Panel.
     *
     * - Setzt das Layout von BoardView auf BorderLayout.
     * - Fügt das Hauptpanel der BoardView hinzu.
     * - Setzt Abstände und die Sichtbarkeit des Panels.
     */
    private void configureBoardView() {
        this.setLayout(new BorderLayout());
        this.add(mainPanel, BorderLayout.CENTER);
        this.setBorder(BorderFactory.createEmptyBorder(25, 25, 0, 25));
        this.setVisible(true);
    }

    /**
     * @brief Erstellt ein Grid-Panel für das Spielbrett.
     *
     * Diese Methode erstellt ein JPanel mit einem Gitterlayout in den Abmessungen des Spielbretts.
     * Für jede Zelle des Gitters wird ein `JLabel` erstellt, gestylt und dem Panel hinzugefügt.
     * Das Panel wird anschließend zurückgegeben.
     *
     * @return Ein JPanel, das das Grid-Layout des Spielbretts enthält.
     */
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

    /**
     * @brief Erstellt ein Panel mit numerischen Beschriftungen für die Spalten des Spielbretts.
     *
     * Diese Methode erstellt ein JPanel, das eine Reihe von numerischen Labels (1 bis 10) enthält,
     * die als Beschriftung für die Spalten des Spielbretts dienen.
     *
     * @return Ein JPanel, das die numerischen Beschriftungen enthält.
     */
    private JPanel createNumericLabelsPanel() {
        JPanel labelsPanel = new JPanel(new GridLayout(1, 10));

        JLabel emptyLabel = new JLabel("", SwingConstants.CENTER);
        emptyLabel.setPreferredSize(new Dimension(10, 10));
        labelsPanel.add(emptyLabel);

        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for (int number : numbers) {
            JLabel label = new JLabel(String.valueOf(number), SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(20, 20));
            labelsPanel.add(label);
        }

        return labelsPanel;
    }

    /**
     * @brief Erstellt ein Panel mit alphabetischen Beschriftungen für die Reihen des Spielbretts.
     *
     * Diese Methode erstellt ein JPanel, das eine Spalte von alphabetischen Labels (A bis J) enthält,
     * die als Beschriftung für die Reihen des Spielbretts dienen.
     *
     * @return Ein JPanel, das die alphabetischen Beschriftungen enthält.
     */
    private JPanel createAlphabeticLabelsPanel() {
        JPanel labelsPanel = new JPanel(new GridLayout(10, 1));
        char[] labels = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        for (char labelChar : labels) {
            JLabel label = new JLabel(String.valueOf(labelChar), SwingConstants.CENTER);
            label.setPreferredSize(new Dimension(this.CELL_SIZE, this.CELL_SIZE));
            labelsPanel.add(label);
        }
        return labelsPanel;
    }

    /**
     * @brief Erstellt ein gestyltes JLabel für eine Zelle im Spielbrett.
     *
     * Diese Methode erzeugt ein `JLabel` mit bestimmten Stileigenschaften,
     * wie Hintergrundfarbe, Rand und Ausrichtung. Zusätzlich wird ein `MouseListener`
     * hinzugefügt, der auf Klicks reagiert und die Position der geklickten Zelle an
     * einen registrierten `BoardClickListener` weitergibt.
     *
     * @param row Die Zeile der Zelle im Spielbrett.
     * @param col Die Spalte der Zelle im Spielbrett.
     * @return Ein gestyltes `JLabel`, das als Zelle im Spielbrett dient.
     */
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
    /**
     * @brief Fügt numerische und alphabetische Beschriftungen zum Spielbrett hinzu.
     *
     * Diese Methode fügt dem `mainPanel` die numerischen Beschriftungen für die Spalten (oben)
     * und die alphabetischen Beschriftungen für die Reihen (links) des Spielbretts hinzu.
     */
    public void createLabelForBoard() {
        mainPanel.add(createNumericLabelsPanel(), BorderLayout.NORTH);
        mainPanel.add(createAlphabeticLabelsPanel(), BorderLayout.WEST);
    }

    /**
     * @brief Erstellt das Panel zur Schiffsplatzierung.
     *
     * Diese Methode initialisiert das Grid-Panel für die Schiffsplatzierung, zeichnet die Rasterlinien,
     * fügt eine Vorschau für die Schiffsplatzierung hinzu und füllt das Grid mit den Zellen des Spielfelds.
     */
    public void createPanelForShipPlacement() {
        resetGridPanel();
        JPanel customGridPanel = createCustomGridPanel();
        initializeShipPreviewLabel(customGridPanel);
        addLabelsToGridPanel(customGridPanel);
        addCustomGridPanelToGridPanel(customGridPanel);
    }

    /**
     * @brief Erstellt ein benutzerdefiniertes Grid-Panel für die Schiffsplatzierung.
     *
     * Diese Methode erstellt ein JPanel, das die Rasterlinien für das Spielfeld zeichnet.
     * Das Panel ist transparent und ermöglicht das Platzieren von JLabels für die Zellen des Spielfelds.
     *
     * @return Ein JPanel mit benutzerdefinierter Zeichnung der Rasterlinien.
     */
    private JPanel createCustomGridPanel() {
        JPanel customGridPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGridLines(g);
            }
        };
        customGridPanel.setOpaque(false);
        return customGridPanel;
    }

    /**
     * @brief Initialisiert das Label zur Vorschau der Schiffsplatzierung.
     *
     * Diese Methode erstellt ein JLabel, das als Vorschau für die Platzierung eines Schiffs dient.
     * Das Label wird transparent gemacht und zum angegebenen Panel hinzugefügt.
     *
     * @param panel Das JPanel, dem das Vorschau-Label hinzugefügt wird.
     */
    private void initializeShipPreviewLabel(JPanel panel) {
        shipPreviewLabel = new JLabel();
        shipPreviewLabel.setOpaque(true);
        shipPreviewLabel.setBackground(new Color(110, 110, 255, 255));
        shipPreviewLabel.setVisible(false);
        panel.add(shipPreviewLabel);
    }

    /**
     * @brief Füllt das Grid-Panel mit Zellen für das Spielfeld.
     *
     * Diese Methode erzeugt für jede Zelle des Spielfelds ein JLabel, setzt das Styling und fügt es
     * dem angegebenen Panel hinzu. Die Labels werden im 2D-Array `labels` gespeichert.
     *
     * @param panel Das JPanel, dem die Labels hinzugefügt werden.
     */
    private void addLabelsToGridPanel(JPanel panel) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                JLabel label = createStyledLabel(i, j);
                labels[i][j] = label;
                panel.add(label);
            }
        }
    }

    /**
     * @brief Fügt das benutzerdefinierte Grid-Panel zum Haupt-Grid-Panel hinzu.
     *
     * Diese Methode fügt das erstellte benutzerdefinierte Grid-Panel, das die Zellen und Rasterlinien enthält,
     * dem Grid-Panels hinzu.
     *
     * @param customGridPanel Das benutzerdefinierte Grid-Panel, das hinzugefügt wird.
     */
    private void addCustomGridPanelToGridPanel(JPanel customGridPanel) {
        this.gridPanel.add(customGridPanel, BorderLayout.CENTER);
    }


    /**
     * @brief Aktualisiert das gesamte Spielbrett basierend auf dem aktuellen Zustand des `BoardModel`.
     *
     * Diese Methode durchläuft alle Zellen des Spielbretts und aktualisiert sie entsprechend dem übergebenen
     * `BoardModel`, indem sie die Methode `updateCell` für jede Zelle aufruft.
     *
     * @param playerBoard Das `BoardModel`, das den aktuellen Zustand des Spielbretts enthält.
     */
    public void updateBoard(BoardModel playerBoard) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                updateCell(row, col, playerBoard);
            }
        }
    }

    /**
     * @brief Aktualisiert die Vorschau eines Schiffs auf dem Spielbrett.
     *
     * Diese Methode setzt die Position und Größe des `shipPreviewLabel`, um eine Vorschau des Schiffes
     * an der angegebenen Position (x, y) mit der angegebenen Breite und Höhe anzuzeigen.
     *
     * @param x Die x-Koordinate der Vorschau.
     * @param y Die y-Koordinate der Vorschau.
     * @param width Die Breite der Vorschau.
     * @param height Die Höhe der Vorschau.
     */
    public void updateShipPreview(int x, int y, int width, int height) {
        shipPreviewLabel.setBounds(x, y, width, height);
        shipPreviewLabel.setVisible(true);
    }

    /**
     * @brief Aktualisiert die Darstellung eines aufgedeckten Schiffs.
     *
     * Diese Methode wird verwendet, um die grafische Darstellung eines Schiffs, das vollständig aufgedeckt wurde,
     * auf dem Spielbrett zu aktualisieren. Dabei wird das Icon und die Hintergrundfarbe der Zellen,
     * die das Schiff belegen, entsprechend geändert.
     *
     * @param ship Das `ShipModel`-Objekt, das das aufgedeckte Schiff repräsentiert.
     */
    public void updateRevealedShip(ShipModel ship) {
        for (CellModel cell : ship.getShipCells()) {
            JLabel cellLabel = getLabelForCell(cell.getX(), cell.getY());
            cellLabel.setIcon(IconView.createCrossIcon(Color.RED, CELL_SIZE / 2));
            cellLabel.setBackground(Color.RED);
        }
    }

    /**
     * @brief Aktualisiert die grafische Darstellung einer Zelle basierend auf ihrem Zustand im Spielmodell.
     *
     * Diese Methode wird verwendet, um den Zustand einer Zelle auf dem Spielbrett anzuzeigen.
     * Abhängig vom Zustand der Zelle im übergebenen `BoardModel` (frei, gesetzt oder getroffen)
     * wird das entsprechende visuelle Darstellung (Farbe, Icon) auf das zugehörige `JLabel` angewendet.
     *
     * @param row Die Zeile der Zelle, die aktualisiert werden soll.
     * @param col Die Spalte der Zelle, die aktualisiert werden soll.
     * @param playerBoard Das `BoardModel`, das den aktuellen Zustand der Zelle enthält.
     */
    private void updateCell(int row, int col, BoardModel playerBoard) {
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

    /**
     * @brief Setzt die Hintergrundfarbe der Zelle auf weiß, um sie als freie Zelle darzustellen.
     *
     * Diese Methode wird verwendet, um eine Zelle als "frei" zu markieren, indem der Hintergrund auf Weiß gesetzt wird.
     *
     * @param label Das JLabel der zu aktualisierenden Zelle.
     */
    private void updateFreeCell(JLabel label) {
        label.setBackground(Color.WHITE);
    }

    /**
     * @brief Setzt die Hintergrundfarbe der Zelle auf blau, um sie als belegte Zelle darzustellen.
     *
     * Diese Methode wird verwendet, um eine Zelle als "belegt" zu markieren, indem der Hintergrund auf Blau gesetzt wird.
     *
     * @param label Das JLabel der zu aktualisierenden Zelle.
     */
    private void updateSetCell(JLabel label) {
        label.setBackground(Color.BLUE);
    }

    /**
     * @brief Aktualisiert die Zelle, um einen Treffer anzuzeigen, indem ein rotes Kreuz als Icon gesetzt wird.
     *
     * Diese Methode wird verwendet, um eine Zelle als "getroffen" zu markieren, indem ein rotes Kreuz auf dem Label angezeigt wird.
     *
     * @param label Das JLabel der zu aktualisierenden Zelle.
     */
    private void updateHitCell(JLabel label) {
        label.setIcon(IconView.createCrossIcon(Color.RED, CELL_SIZE / 4));
    }

    /**
     * @brief Blendet die Vorschau für die Schiffplatzierung aus.
     *
     * Diese Methode setzt die Sichtbarkeit des Labels, das für die Vorschau der Schiffplatzierung verwendet wird, auf unsichtbar (false).
     */
    public void hideShipPreview() {
        shipPreviewLabel.setVisible(false);
    }


    //Temporäre Anzeige
    public void addGraphicsToCells(int startX, int startY, int length, boolean horizontal) {
        for (int i = 0; i < length; i++) {
            int x = horizontal ? startX : startX + i;
            int y = horizontal ? startY + i : startY;

            if (x < BOARD_SIZE && y < BOARD_SIZE) {
                JLabel graphicLabel = new JLabel();
                graphicLabel.setOpaque(true);
                graphicLabel.setBackground(new Color(0, 21, 255, 128));
                graphicLabel.setBounds(y * CELL_SIZE, x * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                graphicsLabels.add(graphicLabel);
                gridPanel.add(graphicLabel);
            }
        }
    }

    /**
     * @brief Schaltet die Sichtbarkeit des Spielbretts und der Zellen um.
     *
     * Diese Methode ändert die Sichtbarkeit des gesamten Spielbretts und der einzelnen Zellen.
     * Wenn das `gridPanel` sichtbar ist, werden die Zellen ausgeblendet, und umgekehrt.
     *
     * @param visible Ein boolean-Wert, der angibt, ob das `gridPanel` sichtbar sein soll (true) oder nicht (false).
     */
    public void toggleGridVisibility(boolean visible) {
        gridPanel.setVisible(visible);

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                labels[i][j].setVisible(!visible);
            }
        }
    }


    /**
     * @brief Markiert eine Zelle des Spielbretts als Fehlschuss.
     *
     * Diese Methode aktualisiert das angegebene `JLabel`, um es als Fehlschuss zu kennzeichnen.
     * Dabei wird ein kleines schwarzes Punkt-Icon in die Zelle eingefügt.
     *
     * @param label Das `JLabel`, das als Fehlschuss markiert werden soll.
     */
    public void markAsMiss(JLabel label) {
        label.setIcon(IconView.createPointIcon(Color.BLACK, CELL_SIZE / 4));
    }


    /**
     * @brief Zeichnet die Gitterlinien auf dem Spielbrett.
     *
     * Diese Methode wird verwendet, um die vertikalen und horizontalen Gitterlinien des Spielbretts zu zeichnen.
     * Die Linien werden basierend auf der Größe des Spielbretts (`BOARD_SIZE`) und der Größe der Zellen (`CELL_SIZE`) berechnet und gezeichnet.
     *
     * @param g Das `Graphics`-Objekt, das zum Zeichnen der Gitterlinien verwendet wird.
     */
    private void drawGridLines(Graphics g) {
        g.setColor(new Color(200, 200, 200));  // Setzt die Farbe der Gitterlinien auf ein helles Grau

        // Zeichnet die vertikalen Gitterlinien
        for (int i = 0; i <= BOARD_SIZE; i++) {
            int x = i * CELL_SIZE;
            g.drawLine(x, 0, x, BOARD_SIZE * CELL_SIZE);
        }

        // Zeichnet die horizontalen Gitterlinien
        for (int i = 0; i <= BOARD_SIZE; i++) {
            int y = i * CELL_SIZE;
            g.drawLine(0, y, BOARD_SIZE * CELL_SIZE, y);
        }
    }



    /**
     * @brief Entfernt das Panel zur Schiffsplatzierung und stellt das Standard-Spielfeld wieder her.
     *
     * Diese Methode entfernt alle temporären Grafiken, die während der Schiffsplatzierung verwendet wurden,
     * und ersetzt das aktuelle Gitterpanel durch ein neues, leeres Panel. Danach wird das Spielfeld anhand
     * des übergebenen `BoardModel` mit den entsprechenden Inhalten neu aufgebaut.
     *
     * @param boardModel Das `BoardModel`, das die Daten für das wiederhergestellte Spielfeld liefert.
     */
    public void removePanelForShipPlacement(BoardModel boardModel) {
        removeGraphics();
        gridPanel.removeAll();
        this.mainPanel.remove(gridPanel);
        this.mainPanel.add(createGridPanel());
        this.updateBoard(boardModel);
    }

    public void removeGraphics() {
        for (JLabel graphicLabel : graphicsLabels) {
            gridPanel.remove(graphicLabel);
        }
    }

    /**
     * @brief Setzt das Grid-Panel zurück und konfiguriert es für die Schiffsplatzierung.
     *
     * Diese Methode entfernt alle vorhandenen Komponenten aus dem Grid-Panel,
     * setzt das Layout auf BorderLayout und stellt sicher, dass das Panel eine definierte Größe hat.
     */
    private void resetGridPanel() {
        this.gridPanel.removeAll();
        this.gridPanel.setLayout(new BorderLayout());
        this.gridPanel.setPreferredSize(new Dimension(60, 170));
    }

    /**
     * @interface BoardClickListener
     * @brief Listener für Klicks auf Spielbrett-Zellen.
     *
     * Definiert eine Methode, die aufgerufen wird, wenn eine Zelle angeklickt wird.
     */
    public interface BoardClickListener {

        /**
         * @brief Reagiert auf einen Klick auf eine Spielbrett-Zelle.
         *
         * @param row Die Reihe der angeklickten Zelle.
         * @param col Die Spalte der angeklickten Zelle.
         * @param label Das JLabel der angeklickten Zelle.
         */
        void onCellClicked(int row, int col, JLabel label);
    }
}
