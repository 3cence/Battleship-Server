package Battleship;

import EmNet.Connection;
import EmNet.Packet;
import EmNet.PacketType;

import java.util.ArrayList;

public class BattleshipGameRoom extends Thread {
    private ArrayList<User> players;
    private ArrayList<User> spectators;
    private final String roomName, id;
    public BattleshipGameRoom(String roomName, int id) {
        this.roomName = roomName;
        this.id = String.valueOf(id);
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
    public String getRoomName() {
        return roomName;
    }
    public String getRoomId() {
        return id;
    }
    @Override
    public void run() {

    }
}
