package Battleship;

import EmNet.Packet;
import Network.NetworkHandler;
import Network.PacketData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BattleshipGameRoom extends Thread {
    private List<User> players;
    private List<User> spectators;
    private final String roomName, id;
    public BattleshipGameRoom(String roomName, int id) {
        this.roomName = roomName;
        this.id = String.valueOf(id);
        players = Collections.synchronizedList(new ArrayList<>());
        spectators = Collections.synchronizedList(new ArrayList<>());
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
        u.setMode(mode);
        u.getConnection().onConnectionEnd(e->{
            if (u.getMode().equals("player"))
                players.remove(u);
            if (u.getMode().equals("spectator"))
                spectators.remove(u);
        });
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
    private User player(int i) {
        return players.get(i);
    }
    @Override
    public void run() {
        // Wait for 2 players to be in the room
        while (players.size() != 2) {}
        System.out.println("Game started with user " + players.get(0).getName() + " and user " + players.get(1).getName());
        // Get ship placements for both players
        int readyBoards = 0;
        while (readyBoards < players.size()) {
            for (User u: players) {
                if (u.getConnection().hasNextPacket()) {
                    List<PacketData> p = NetworkHandler.extractPacketData(u.getConnection().getNextPacket());
                    if (p.get(0).type().equals("ship_placement")) {
                        u.getBoard().placeShips(p);
                        u.getConnection().sendPacket(NetworkHandler.generatePacketData
                                ("waiting_on_players", "" + (players.size() - readyBoards)));
                    }
                    else {
                        u.getConnection().sendPacket(NetworkHandler.generatePacketData
                                ("ignored","ship_placement"));
                    }
                }
            }
        }
        // Begin the game

    }
}
