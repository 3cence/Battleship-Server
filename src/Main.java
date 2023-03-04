import Battleship.BattleshipGameRoom;
import EmNet.Server;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(48863);
        ArrayList<BattleshipGameRoom> activeGames = new ArrayList<>();
        ArrayList<BattleshipGameRoom> activeUsers = new ArrayList<>();
        server.start();
        while (true) {
            if (server.hasNewConnections()) {

            }
        }
    }
}