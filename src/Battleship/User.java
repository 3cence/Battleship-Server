package Battleship;

import Battleship.Game.Board;
import EmNet.Connection;

public class User {
    private final Connection connection;
    private final Board board;
    private String name;
    private String mode;
    public User(Connection c) {
        connection = c;
        board = new Board();
        name = "Guest";
        mode = "";
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    /**
     * player/spectator
     * @param mode player/spectator
     */
    public void setMode(String mode) {
        this.mode = mode;
    }
    public String getMode() {
        return mode;
    }
    public Connection getConnection() {
        return connection;
    }

    public Board getBoard() {
        return board;
    }
}
