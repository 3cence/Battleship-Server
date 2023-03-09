package Battleship;

import Network.NetworkHandler;
import Network.PacketData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BattleshipGameRoom extends Thread {
    private final List<User> players;
    private final List<User> spectators;
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
    public int getPlayerCount() {
        return players.size();
    }
    private User player(int i) {
        return players.get(i);
    }
    @Override
    public void run() {
        // Wait for 2 players to be in the room
        while (players.size() != 2) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // TODO: add room timeout
        }
        System.out.println("Game started with user " + players.get(0).getName() + " and user " + players.get(1).getName());
        //TODO: send game_start packet to both clients
        // Get ship placements for both players
        int readyBoards = 0;
        while (readyBoards < players.size()) {
            for (User u: players) {
                if (u.getConnection().hasNextPacket()) {
                    List<PacketData> p = NetworkHandler.extractPacketData(u.getConnection().getNextPacket());
                    if (p.get(0).type().equals("ship_placement")) {
                        if (u.getBoard().placeShips(p)) {
                            readyBoards++;
                            u.getConnection().sendPacket(NetworkHandler.generatePacketData
                                    ("waiting_on_players", "" + (players.size() - readyBoards)));
                            break;
                        }
                    }
                    u.getConnection().sendPacket(NetworkHandler.generatePacketData
                            ("ignored","ship_placement"));
                }
            }
        }
        // Begin the game
        // use "current" for current player and "1 - current" for not current
        int current = 0;
        player(current).getConnection().sendPacket(NetworkHandler.generatePacketData("your_turn"));
        while (player(0).getBoard().shipsLeft() > 0 && player(1).getBoard().shipsLeft() > 0) {
            if (player(current).getConnection().hasNextPacket()) {
                PacketData p = NetworkHandler.extractPacketData(player(current).getConnection().getNextPacket()).get(0);
                switch (p.type()) {
                    case "attack":
                        try {
                            int x = Integer.parseInt(p.data().split(",")[0]);
                            int y = Integer.parseInt(p.data().split(",")[1]);
                            boolean attackResult = player(1 - current).getBoard().attackTile(x, y);
                            if (attackResult) {
                                player(current).getConnection().sendPacket(NetworkHandler.generatePacketData
                                        ("attack_results",x + "," + y + "hit," + player(1 - current).getBoard().shipsLeft()));
                                player(1 - current).getConnection().sendPacket(NetworkHandler.generatePacketData
                                        ("opponent_attacked", x + "," + y + ",hit," + player(1 - current).getBoard().shipsLeft()));
                            }
                            else {
                                player(current).getConnection().sendPacket(NetworkHandler.generatePacketData
                                        ("attack_results",x + "," + y + "miss," + player(1 - current).getBoard().shipsLeft()));
                                player(1 - current).getConnection().sendPacket(NetworkHandler.generatePacketData
                                        ("opponent_attacked", x + "," + y + ",miss," + player(1 - current).getBoard().shipsLeft()));
                            }
                            current = 1 - current;
                            player(current).getConnection().sendPacket(NetworkHandler.generatePacketData
                                    ("your_turn"));
                        } catch (RuntimeException e) {
                            player(current).getConnection().sendPacket(NetworkHandler.generatePacketData
                                    ("invalid_attack"));
                        }
                        break;
                    case "request_reload":
                        // TODO: reload data
                        break;
                }
            }
        }
        // End the game
        // TODO: send opponent's board
        if (player(0).getBoard().shipsLeft() == 0) {
            player(0).getConnection().sendPacket(NetworkHandler.generatePacketData
                    ("game_over","lose"));
            player(1).getConnection().sendPacket(NetworkHandler.generatePacketData
                    ("game_over","win"));
        }
        else if (player(1).getBoard().shipsLeft() == 0) {
            player(1).getConnection().sendPacket(NetworkHandler.generatePacketData
                    ("game_over", "lose"));
            player(0).getConnection().sendPacket(NetworkHandler.generatePacketData
                    ("game_over", "win"));
        }
        else {
            player(1).getConnection().sendPacket(NetworkHandler.generatePacketData
                    ("game_over", "abort"));
            player(0).getConnection().sendPacket(NetworkHandler.generatePacketData
                    ("game_over", "abort"));
        }
        try {
            sleep(30000);
            // TODO: End connections and remove this room from active rooms
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
