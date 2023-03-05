package Battleship.Game;

import Network.PacketData;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int size = 10;
    private static final int ships = 5;
    private int shipsRemaining;
    private Tile[][] shipBoard;

    public Board() {
        shipsRemaining = 0;
    }

    /**
     * the entire ship_placement packet
     * @param shipPacket the entire ship_placement packet
     */
    public void placeShips(List<PacketData> shipPacket) {
        PacketData headerPacket = shipPacket.remove(0);
        if (!headerPacket.type().equals("ship_placement") || !headerPacket.data().equals("" + ships))
            throw new RuntimeException("This is not a valid ship placement packet, has type " + headerPacket.type() + " & data " + headerPacket.data());
        shipBoard = new Tile[10][10];
        ArrayList<ShipType> usedTypes = new ArrayList<>();
        for (PacketData ship: shipPacket) {
            ShipType type = ShipType.fromString(ship.type());
            if (usedTypes.contains(type))
                throw new RuntimeException("Duplicate Ship types");
            usedTypes.add(type);
            // 0 = x, 1 = y, 2 = horizontal/vertical
            String[] positionInfo = ship.data().split(",");
            int x = Integer.parseInt(positionInfo[0].substring(1));
            int y = Integer.parseInt(positionInfo[1].substring(1));
            boolean isHorizontal = positionInfo[2].equals("horizontal");
            for (int i = 0; i < type.holes(); i++) {
                shipsRemaining++;
                if (x+i < size && y+i < size) {
                    if (isHorizontal)
                        shipBoard[y][x + i].makeShip(type);
                    else
                        shipBoard[y + i][x].makeShip(type);
                }
                else
                    throw new RuntimeException("Invalid ship position");
            }
        }
    }

    /**
     * Attacks given tile and returns if the attack is a hit?
     * @param x x
     * @param y y
     * @return y/n
     */
    public boolean attackTile(int x, int y) {
        Tile target = shipBoard[y][x];
        switch (target.hit()) {
            case Tile.HIT:
                return true;
            case Tile.MISS:
                return false;
        }
        // realisticly shouldn't get here
        return false;
    }
}
