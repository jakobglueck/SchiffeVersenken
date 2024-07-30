package model;


import utils.CellState;

public class BoardModel {
    private CellModel[][] board;

    public BoardModel() {
        this.board = new CellModel[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.board[i][j] = new CellModel();
            }
        }
    }

    public void setBoard(int x, int y, CellState cellState) {
        this.board[x][y].setCellValue(cellState);
    }
    
    public CellModel[][] getCompletBoard() {
        return this.board;
    }

    public CellModel getCell(int x, int y) {
        return this.board[x][y];
    }
}
