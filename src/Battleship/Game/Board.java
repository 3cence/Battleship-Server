package Battleship.Game;

import Network.PacketData;

import java.util.List;

public class Board {
    private static final int size = 10;
    private static final int ships = 5;
    private Tile[][] shipBoard;

    public Board() {
        shipBoard = new Tile[10][10];
    }
    public void placeShips(List<PacketData> shipPacket) {
        PacketData headerPacket = shipPacket.remove(0);
        if (!headerPacket.type().equals("ship_placement") || !headerPacket.data().equals("" + ships))
            throw new RuntimeException("This is not a valid ship placement packet, has type " + headerPacket.type() + " & data " + headerPacket.data());
        for (PacketData ship: shipPacket) {
            ShipType type = ShipType.fromString(ship.type());
            // 0 = x, 1 = y, 2 = horizontal/vertical
            String[] positionInfo = ship.data().split(",");
            int x = Integer.parseInt(positionInfo[0].substring(1));
            int y = Integer.parseInt(positionInfo[1].substring(1));
            boolean isHorizontal = positionInfo[2].equals("horizontal");
            for (int i = 0; i < type.holes(); i++) {
                if (isHorizontal) {
                    // TODO: Set tiles accordingly
                }
                else {

                }
            }
        }
    }
}
