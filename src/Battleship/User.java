package Battleship;

import EmNet.Connection;

public class User {
    private final Connection connection;
    private BattleshipGameRoom gameRoom;
    public User(Connection c) {
        connection = c;
    }
    public BattleshipGameRoom getGameRoom() {
        return gameRoom;
    }
    public Connection getConnection() {
        return connection;
    }
    public void joinGameRoom(BattleshipGameRoom gr) {
        if (gameRoom == null) {
            gameRoom = gr;
            gr.joinRoom(this);
        }
        else {
            throw new RuntimeException("Cannot add user. Already assigned to a room");
        }
    }
}
