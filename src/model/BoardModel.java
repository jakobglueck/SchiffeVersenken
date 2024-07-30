package model;


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
}
