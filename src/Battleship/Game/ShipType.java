package Battleship.Game;

public record ShipType(String type, int holes) {
    public static final ShipType CARRIER = new ShipType("carrier", 5);
    public static final ShipType BATTLESHIP = new ShipType("battleship", 4);
    public static final ShipType CRUISER = new ShipType("cruiser", 3);
    public static final ShipType SUBMARINE = new ShipType("submarine", 3);
    public static final ShipType DESTROYER = new ShipType("destroyer", 2);
    public static ShipType fromString(String s) {
        switch (s) {
            case "carrier":
                return ShipType.CARRIER;
            case "battleship":
                return ShipType.BATTLESHIP;
            case "cruiser":
                return ShipType.CRUISER;
            case "submarine":
                return ShipType.SUBMARINE;
            case "destroyer":
                return ShipType.DESTROYER;
            default:
                throw new RuntimeException(s + " is not a ship type!");
        }
    }
}
