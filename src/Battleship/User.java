package Battleship;

import EmNet.Connection;

public class User {
    private final Connection connection;
    private String name;
    private String mode;
    public User(Connection c) {
        connection = c;
        name = "Guest";
        mode = "";
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public String getMode() {
        return mode;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }
    public Connection getConnection() {
        return connection;
    }
}
