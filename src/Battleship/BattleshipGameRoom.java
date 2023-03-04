package Battleship;

import EmNet.Connection;
import EmNet.Packet;
import EmNet.PacketType;

import java.util.ArrayList;

public class BattleshipGameRoom extends Thread {
    private Connection clientConnection;
    private volatile ArrayList<User> players;
    private ArrayList<User> spectators;
    public BattleshipGameRoom(Connection c) {
        clientConnection = c;
        players = new ArrayList<>();
        spectators = new ArrayList<>();
    }
    public synchronized void joinRoom(User u) {
        if (players.size() < 2) {
            players.add(u);
        } else {
            spectators.add(u);
        }
    }
    @Override
    public void run() {
        // Await game start
        while (true) {
            if (clientConnection.hasNextPacket()) {
                Packet p = clientConnection.getNextPacket();
                if (p.getType() == PacketType.REQUEST && p.getData().equals("")) {

                }
            }
        }
    }
}
