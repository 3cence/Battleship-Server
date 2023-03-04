package Battleship;

import EmNet.Connection;

public class User {
    private Connection connection;
    private BattleshipGameRoom gameRoom;
    public User(Connection c) {
        connection = c;
    }
    public BattleshipGameRoom getGameRoom() {
        return gameRoom;
    }
    public void joinGameRoom(BattleshipGameRoom gr) {
        gameRoom = gr;
        gr.joinRoom(this);
    }
}
