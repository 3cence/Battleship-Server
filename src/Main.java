import Battleship.BattleshipGameRoom;
import Battleship.User;
import EmNet.Connection;
import EmNet.Server;
import Network.NetworkHandler;
import Network.PacketData;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static String buildActiveRooms(ArrayList<BattleshipGameRoom> rooms) {
        ArrayList<PacketData> packet = new ArrayList<>();
        for (BattleshipGameRoom room: rooms) {
            packet.add(new PacketData(room.getRoomId(), room.getRoomName()));
        }
        return NetworkHandler.generatePacketData(packet);
    }
    public static void main(String[] args) {
        Server server = new Server(48863);
        ArrayList<BattleshipGameRoom> activeRooms = new ArrayList<>();
        ArrayList<User> newUsers = new ArrayList<>();
        server.start();
        while (true) {
            if (server.hasNewConnections()) {
                User newUser = new User(server.getNewConnection());
                newUser.getConnection().sendPacket(buildActiveRooms(activeRooms));
                newUsers.add(newUser);
            }
            if (!newUsers.isEmpty()) {
                for (User u: newUsers) {
                    Connection c = u.getConnection();
                    if (c.hasNextPacket()) {
                        List<PacketData> pd = NetworkHandler.extractPacketData(c.getNextPacket());
                        switch (pd.get(0).type()) {
                            case "server_select":
                                for (BattleshipGameRoom room: activeRooms) {
                                    if (room.getRoomId().equals(pd.get(0).data())) {
                                        room.joinRoom(u);
                                        newUsers.remove(u);
                                        break;
                                    }
                                }
                                break;
                            case "refresh_rooms":
                                c.sendPacket(buildActiveRooms(activeRooms));
                                break;
                        }
                    }
                }
            }
        }
    }
}