package Battleship;

import EmNet.Connection;
import EmNet.Packet;
import EmNet.PacketType;
import Network.PacketData;

public class BattleshipGame extends Thread {
    private Connection clientConnection;
    public BattleshipGame(Connection c) {
        clientConnection = c;
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
