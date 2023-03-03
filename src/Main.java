import Battleship.BattleshipGame;
import EmNet.Server;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(48863);
        ArrayList<BattleshipGame> activeGames = new ArrayList<>();
        server.start();
        while (true) {
            if (server.hasNewConnections()) {
//                BattleshipGame newGame = new BattleshipGame(server.getNewConnection());
//                activeGames.add(newGame);
//                newGame.start();
            }
        }
    }
}