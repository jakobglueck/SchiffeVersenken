package model;

import utils.*;

public class CellModel {
    private CellState cellValue;

    public CellModel() {
        this.cellValue = CellState.FREE;
    }

    public CellState getCellValue() {
        return this.cellValue;
    }

    public void setCellValue(CellState cellValue) {
        this.cellValue = cellValue;
    }
}