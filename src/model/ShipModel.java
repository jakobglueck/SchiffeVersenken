package model;

import utils.CellState;

import java.util.ArrayList;
import java.util.List;

public class ShipModel {
    private final List<CellModel> shipCells;
    private final int length;
    private final boolean horizontal;
    private boolean sunk;

    public ShipModel(int startX, int startY, int length, boolean horizontal) {
        this.length = length;
        this.horizontal = horizontal;
        this.sunk = false;
        this.shipCells = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            int currentX = horizontal ? startX + i : startX;
            int currentY = horizontal ? startY : startY + i;
            this.shipCells.add(new CellModel(currentX, currentY, CellState.SET));
        }
    }

    public boolean isHit(int x, int y) {
        return shipCells.stream().anyMatch(cell -> cell.getX() == x && cell.getY() == y);
    }

    public void checkShipStatus(BoardModel board) {
        this.sunk = shipCells.stream().allMatch(cell -> {
            CellModel boardCell = board.getCell(cell.getX(), cell.getY());
            return boardCell != null && boardCell.getCellState() == CellState.HIT;
        });
    }

    public boolean isSunk() {
        return this.sunk;
    }

    public int getLength() {
        return this.length;
    }

    public boolean isHorizontal() {
        return this.horizontal;
    }
}