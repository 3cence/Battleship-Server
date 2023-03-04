package Battleship.Game;

public class Tile {
    public static final int HIT = -1;
    public static final int UNTOUCHED = 0;
    public static final int MISS = 1;
    private int state;
    private ShipType ship;
    public Tile() {
        state = UNTOUCHED;
        ship = null;
    }
    public int getState() {
        return state;
    }
    public int hit() {
        if (state != UNTOUCHED)
            throw new RuntimeException("Cannot target same square twice");
        if (isShip())
            state = HIT;
        else
            state = MISS;
        return state;
    }
    public void makeShip(ShipType shipType) {
        ship = shipType;
    }
    public boolean isShip() {
        return ship != null;
    }
}
