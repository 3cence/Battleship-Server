package Battleship.Game;

public record ShipType(String name, int holes) {
    public static final ShipType CARRIER = new ShipType("carrier", 5);
    public static final ShipType BATTLESHIP = new ShipType("battleship", 4);
    public static final ShipType CRUISER = new ShipType("cruiser", 3);
    public static final ShipType SUBMARINE = new ShipType("submarine", 3);
    public static final ShipType DESTROYER = new ShipType("destroyer", 2);
}
