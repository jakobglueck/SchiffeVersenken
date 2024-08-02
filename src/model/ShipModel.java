package model;

import utils.CellState;

import java.util.ArrayList;
import java.util.List;

public class ShipModel {
    private List<CellModel> shipCells;
    private int length;
    private boolean horizontal;
    private boolean shipStatus;

    public ShipModel() {
        this.shipCells = new ArrayList<>();
    }

    public void setShipParameters(CellModel startCell, int length, boolean horizontal) {
        this.length = length;
        this.horizontal = horizontal;
        this.shipStatus = true;
        this.shipCells.clear();

        int startX = startCell.getCellCoordX();
        int startY = startCell.getCellCoordY();

        for (int i = 0; i < length; i++) {
            int currentX = horizontal ? startX + i : startX;
            int currentY = horizontal ? startY : startY + i;
            this.shipCells.add(new CellModel(currentX, currentY, CellState.SET));
        }
    }

    public void changeDirection() {
        this.horizontal = !this.horizontal;
        updateShipCells();
    }

    private void updateShipCells() {
        CellModel startCell = shipCells.get(0);
        int startX = startCell.getCellCoordX();
        int startY = startCell.getCellCoordY();

        for (int i = 0; i < length; i++) {
            CellModel cell = shipCells.get(i);
            int newX = horizontal ? startX + i : startX;
            int newY = horizontal ? startY : startY + i;
            cell.updateCellCordX(newX);
            cell.updateCellCordY(newY);
        }
    }

    public CellModel getStartCell() {
        return shipCells.get(0);
    }

    public CellModel getEndCell() {
        return shipCells.get(shipCells.size() - 1);
    }

    public int getLength() {
        return this.length;
    }

    public boolean getHorizontal() {
        return this.horizontal;
    }

    public boolean getShipStatus() {
        return this.shipStatus;
    }

    public boolean isValidLength() {
        return this.length >= 2 && this.length <= 5;
    }

    public boolean isHit(int x, int y) {
        for (CellModel cell : shipCells) {
            if (cell.getCellCoordX() == x && cell.getCellCoordY() == y) {
                return true;
            }
        }
        return false;
    }

    public void checkShipStatus(BoardModel board) {
        boolean allCellsHit = true;
        for (CellModel cell : shipCells) {
            CellModel boardCell = board.getCell(cell.getCellCoordX(), cell.getCellCoordY());
            if (boardCell.getCellState() != CellState.HIT) {
                allCellsHit = false;
                break;
            }
        }
        this.shipStatus = !allCellsHit;
    }
}