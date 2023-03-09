package Battleship.Game;

import Network.PacketData;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final int size = 10;
    private static final int ships = 5;
    private int shipsRemaining;
    private Tile[][] shipBoard;
    private boolean areShipsPlaced;

    public Board() {
        shipsRemaining = 0;
        areShipsPlaced = false;
    }
    private Tile[][] makeNewBoard() {
        Tile[][] t = new Tile[10][10];
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[i].length; j++) {
                t[i][j] = new Tile();
            }
        }
        return t;
    }
    /**
     * the entire ship_placement packet
     * @param shipPacket the entire ship_placement packet
     * @return Were the ships successfully added?
     */
    public boolean placeShips(List<PacketData> shipPacket) {
        PacketData headerPacket = shipPacket.remove(0);
        if (!headerPacket.type().equals("ship_placement") || !headerPacket.data().equals("" + ships))
            throw new RuntimeException("This is not a valid ship placement packet, has type " + headerPacket.type() + " & data " + headerPacket.data());
        shipBoard = makeNewBoard();
        ArrayList<ShipType> usedTypes = new ArrayList<>();
        for (PacketData ship: shipPacket) {
            ShipType type = ShipType.fromString(ship.type());
            if (usedTypes.contains(type))
                return false;
            usedTypes.add(type);
            // 0 = x, 1 = y, 2 = horizontal/vertical
            String[] positionInfo = ship.data().split(",");
            int x = Integer.parseInt(positionInfo[0]);
            int y = Integer.parseInt(positionInfo[1]);
            boolean isHorizontal = positionInfo[2].equals("false");
            for (int i = 0; i < type.holes(); i++) {
                shipsRemaining++;
                if (x+i < size && y+i < size) {
                    if (isHorizontal)
                        shipBoard[y][x + i].makeShip(type);
                    else
                        shipBoard[y + i][x].makeShip(type);
                }
                else
                    return false;
            }
        }
        areShipsPlaced = true;
        return true;
    }

    /**
     * Returns weather all ships were successfully placed
     * @return t/f
     */
    public boolean areShipsPlaced() {
        return areShipsPlaced;
    }

    /**
     * How many ships are still floating?
     * @return # of ships
     */
    public int shipsLeft() {
        return shipsRemaining;
    }

    /**
     * is ship sunk?
     * @param x x
     * @param y y
     * @return t/f
     */
    private boolean isShipSunk(int x, int y) {
        if (!shipBoard[y][x].isShip())
            return true;
        if (shipBoard[y][x].getState() != Tile.HIT)
            return false;
        return isShipSunk(x + 1, y) && isShipSunk(x - 1, y) && isShipSunk(x, y + 1) && isShipSunk(x, y - 1);
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
                if (isShipSunk(x, y))
                    shipsRemaining--;
                return true;
            case Tile.MISS:
                return false;
        }
        // realisticly shouldn't get here
        return false;
    }
}
