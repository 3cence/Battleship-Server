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
        packet.add(new PacketData("room_list", "" + rooms.size()));
        for (BattleshipGameRoom room: rooms) {
            packet.add(new PacketData(room.getRoomName(), room.getRoomId() + "," + room.getPlayerCount()));
        }
        return NetworkHandler.generatePacketData(packet);
    }
    public static void main(String[] args) {
        Server server = new Server(48863);
        int roomIds = 0;
        ArrayList<BattleshipGameRoom> activeRooms = new ArrayList<>();
        ArrayList<User> newUsers = new ArrayList<>();
        server.start();
        while (true) {
            if (server.hasNewConnections()) {
                User newUser = new User(server.getNewConnection());
                newUser.getConnection().sendPacket(NetworkHandler.generatePacketData("name_request"));
                newUser.getConnection().sendPacket(buildActiveRooms(activeRooms));
                newUsers.add(newUser);
                System.out.println("New user joined");
            }
            if (!newUsers.isEmpty()) {
                ArrayList<User> remove = new ArrayList<>();
                for (User u: newUsers) {
                    Connection c = u.getConnection();
                    if (c.hasNextPacket()) {
                        List<PacketData> pd = NetworkHandler.extractPacketData(c.getNextPacket());
                        System.out.println("User sent data: " + pd.get(0).type() + ":" + pd.get(0).data());
                        switch (pd.get(0).type()) {
                            case "name":
                                u.setName(pd.get(0).data());
                                break;
                            case "join_room":
                                for (BattleshipGameRoom room: activeRooms) {
                                    if (room.getRoomId().equals(pd.get(0).data())) {
                                        room.joinRoom(u);
                                        remove.add(u);
                                        break;
                                    }
                                }
                                break;
                            case "make_room":
                                activeRooms.add(new BattleshipGameRoom(pd.get(0).data(), roomIds));
                                activeRooms.get(activeRooms.size() - 1).start();
                                roomIds++;
                            case "refresh_rooms":
                                c.sendPacket(buildActiveRooms(activeRooms));
                                break;
                        }
                    }
                }
                while (remove.size() > 0) {
                    newUsers.remove(remove.remove(0));
                }
            }
        }
    }
}