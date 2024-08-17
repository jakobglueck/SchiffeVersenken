package model;

import utils.CellState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerPlayerModel extends PlayerModel {

    private List<int[]> availableMoves;
    private int lastMoveX;
    private int lastMoveY;

    public ComputerPlayerModel(String playerName) {
        super(playerName);
        this.initializeAvailableMoves();
    }

    private void initializeAvailableMoves() {
        availableMoves = new ArrayList<>();
        for (int i = 0; i < BoardModel.WIDTH; i++) {
            for (int j = 0; j < BoardModel.HEIGHT; j++) {
                availableMoves.add(new int[]{i, j});
            }
        }
    }

    @Override
    public void takeTurn(PlayerModel opponent, int x, int y) {
        lastMoveX = x;
        lastMoveY = y;
        super.takeTurn(opponent, x, y);
    }

    public int getLastMoveX() {
        return lastMoveX;
    }

    public int getLastMoveY() {
        return lastMoveY;
    }

    public boolean makeMove(PlayerModel opponent) {
        if (!availableMoves.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(availableMoves.size());
            int[] move = availableMoves.remove(index);
            lastMoveX = move[0];
            lastMoveY = move[1];

            BoardModel opponentBoard = opponent.getBoard();
            CellModel targetCell = opponentBoard.getCell(move[0], move[1]);

            if (targetCell.getCellState() == CellState.FREE) {
                return false;
            } else
                return targetCell.getCellState() == CellState.SET;
        }
        return false;
    }

}
