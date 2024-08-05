package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerPlayerModel extends PlayerModel {

    private List<int[]> availableMoves;

    public ComputerPlayerModel(String playerName) {
        super(playerName);
        initializeAvailableMoves();
    }

    private void initializeAvailableMoves() {
        availableMoves = new ArrayList<>();
        for (int i = 0; i < BoardModel.WIDTH; i++) {
            for (int j = 0; j < BoardModel.HEIGHT; j++) {
                availableMoves.add(new int[]{i, j});
            }
        }
    }

    public void makeMove(PlayerModel opponent) {
        if (!availableMoves.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(availableMoves.size());
            int[] move = availableMoves.remove(index);
            takeTurn(opponent, move[0], move[1]);
        }
    }
}
