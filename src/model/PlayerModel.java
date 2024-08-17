package model;

import utils.CellState;


public class PlayerModel {
    private final String playerName;
    private BoardModel board;
    private PlayerStatus playerStatus;

    public PlayerModel(String playerName) {
        this.playerName = playerName;
        this.board = new BoardModel();
        this.playerStatus = new PlayerStatus();
    }

    public String getPlayerName() {
        return playerName;
    }

    public BoardModel getBoard() {
        return board;
    }

    public boolean makeMove(PlayerModel opponent, int x, int y) {
        if (!this.isValidMove(x, y)) {
            return false;
        }
        boolean hit = opponent.getBoard().registerHit(x, y) != null;
        return true;
    }

    public void takeTurn(PlayerModel opponent, int x, int y) {
        if (makeMove(opponent, x, y)) {
            System.out.println(playerName + " made a move.");
        } else {
            System.out.println("Invalid move. Please try again.");
        }
    }

    private boolean isValidMove(int x, int y) {
        if (x < 0 || x >= BoardModel.WIDTH || y < 0 || y >= BoardModel.HEIGHT) {
            return false;
        }

        CellState currentCellState = board.getCell(x, y).getCellState();
        return currentCellState != CellState.HIT;
    }

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    public void reset() {
        board.reset();
        playerStatus.reset();
    }
}
