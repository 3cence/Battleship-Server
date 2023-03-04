package Battleship.Game;

public class Board {
    private static final int size = 10;
    private Tile[][] targetBoard, shipBoard;

    public Board() {
        targetBoard = new Tile[10][10];
        shipBoard = new Tile[10][10];
    }
}
