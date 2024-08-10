package model;

import utils.CellState;


public class PlayerModel {
    private String playerName;
    private BoardModel board;
    private int nextShipIndex;
    private PlayerStatus playerStatus;

    public PlayerModel(String playerName) {
        this.playerName = playerName;
        this.board = new BoardModel();
        this.nextShipIndex = 0;
    }

    public String getPlayerName() {
        return playerName;
    }

    public BoardModel getBoard() {
        return board;
    }

    public boolean placeNextShip(int startX, int startY, boolean horizontal) {
        if (nextShipIndex > BoardModel.BOAT_SIZES.length) {
            return false;
        }
        int length = BoardModel.BOAT_SIZES[nextShipIndex];
        boolean placed = board.placeShip(startX, startY, horizontal, length);
        if (placed) {
            nextShipIndex++;
        }
        return placed;
    }

    public boolean allShipsPlaced() {
        return nextShipIndex == BoardModel.BOAT_SIZES.length;
    }

    public boolean makeMove(PlayerModel opponent, int x, int y) {
        if (!this.isValidMove(x, y)) {
            return false;
        }
        boolean hit = false;
        for(ShipModel ship : this.board.getPlayerShips()){
            if(ship.isHit(x,y)){
                hit = true;
            }
        }
        if (hit) {
            System.out.println(playerName + " hit a target!");
        } else {
            System.out.println(playerName + " missed.");
        }
        this.playerStatus.updateTotalClicks();
        this.playerStatus.calculateHits(this.board);
        this.playerStatus.calculateShunkShips(this.board);
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
        return currentCellState != CellState.HIT && currentCellState != CellState.FREE;
    }

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }
}
