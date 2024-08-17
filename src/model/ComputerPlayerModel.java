package model;

import utils.CellState;

import java.util.*;

public class ComputerPlayerModel extends PlayerModel {

    private List<int[]> computerMove;
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
                computerMove.add(new int[]{i, j});
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
        boolean result = false;
        if (!computerMove.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(computerMove.size());
            int[] move = computerMove.remove(index);
            lastMoveX = move[0];
            lastMoveY = move[1];

            BoardModel opponentBoard = opponent.getBoard();
            CellModel targetCell = opponentBoard.getCell(move[0], move[1]);

            if (targetCell.getCellState() == CellState.FREE) {
                result = false;
            } else {
                result =  targetCell.getCellState() == CellState.SET;
                targetCell.updateCellState(CellState.HIT);
            }
        }
        return result;
    }
}