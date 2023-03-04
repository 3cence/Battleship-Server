package Battleship;

import Network.NetworkHandler;
import Network.PacketData;

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
        String mode;
        if (players.size() < 2) {
            players.add(u);
            mode = "player";
        } else {
            spectators.add(u);
            mode = "spectator";
        }
        ArrayList<PacketData> packet = new ArrayList<>();
        packet.add(new PacketData("joined_room", getRoomName()));
        packet.add(new PacketData("game_mode", mode));
        packet.add(new PacketData("players", "" + players.size()));
        packet.add(new PacketData("spectators", "" + spectators.size()));
        u.getConnection().sendPacket(NetworkHandler.generatePacketData(packet));
    }
    public String getRoomName() {
        return roomName;
    }
    public String getRoomId() {
        return id;
    }
    @Override
    public void run() {
        int lastPlayer = 0;
        int lastSpectator = 0;
        while (true) {
            if (players.size() > lastPlayer) {
                System.out.println("New player Joined!");
                lastPlayer++;
            }
            if (spectators.size() > lastSpectator) {
                System.out.println("New spectator Joined!");
                lastSpectator++;
            }
        }
    }
}
