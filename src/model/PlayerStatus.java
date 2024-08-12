package model;

import View.BoardView;

public class PlayerStatus {

    private int totalClicks;
    private int hits;
    private int misses;
    private int shunkShips;

    PlayerStatus(){
        this.totalClicks = 0;
        this.hits = 0;
        this.misses = 0;
        this.shunkShips = 0;
    }
    public int getTotalClicks() {
        return this.totalClicks;
    }
    public void updateTotalClicks() {
        this.totalClicks++;
    }

    public int getHits() {
        return this.hits;
    }

    public void calculateHits(BoardModel boardModel) {
        int temp = 0;
        for (ShipModel ship : boardModel.getPlayerShips()) {
            for (CellModel cell : ship.getShipCells()) {
                if (cell.isHit()) {
                    temp++;
                }
            }
        }
        this.hits = temp;
        this.misses = this.calculateMisses();
    }

    public int getMisses() {
        return this.misses;
    }

    private int calculateMisses(){
        return this.totalClicks - this.hits;
    }

    public int getShunkShips() {
        return this.shunkShips;
    }

    public void calculateShunkShips(BoardModel boardModel) {
        int temp = 0;
        for(ShipModel ship : boardModel.getPlayerShips()){
            if(ship.isSunk()){
                temp++;
            }
        }
        this.shunkShips = temp;
    }

    public void reset() {
        this.hits = 0;    // Zurücksetzen der Treffer
        this.misses = 0;  // Zurücksetzen der Verfehlungen
        this.shunkShips = 0; // Zurücksetzen der versenkten Schiffe
    }
}
