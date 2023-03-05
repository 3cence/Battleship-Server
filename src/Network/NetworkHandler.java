package Network;

import EmNet.DefaultPacket;
import EmNet.Packet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NetworkHandler {
    public static List<PacketData> extractPacketData(Packet p) {
        if (!p.getData().startsWith("{") || !p.getData().endsWith("}"))
            throw new RuntimeException("Invalid Packet Data");
        ArrayList<PacketData> packetData = new ArrayList<>();
        String[] stringPacketData = p.getData().split(";");
        for (String s: stringPacketData) {
            int split = s.indexOf(":");
            packetData.add(new PacketData(s.substring(1, split), s.substring(split + 1, s.length() - 1)));
        }
        return packetData;
    }
    public static String generatePacketData(List<PacketData> list) {
        StringBuilder out = new StringBuilder();
        for (PacketData pd: list) {
            out.append("{").append(pd.type()).append(":").append(pd.data()).append("};");
        }
        out.deleteCharAt(out.length() - 1);
        return out.toString();
    }
    public static String generatePacketData(String type, String data) {
        return "{" + type + ":" + data + "}";
    }
    public static String generatePacketData(String type) {
        return generatePacketData(type, "");
    }
    public static String generatePacketData(PacketData[] list) {
        return generatePacketData(Arrays.stream(list).toList());
    }

    public static void main(String[] args) {
        ArrayList<PacketData> list = new ArrayList<>();
        list.add(new PacketData("GameStart", ""));
        list.add(new PacketData("PlayerName", "Emma"));
        list.add(new PacketData("MoreInfo", "information"));
        System.out.println(generatePacketData(list));
        for (PacketData p: extractPacketData(new DefaultPacket(null, "[6]{GameInfo:Wait2},{GameMode:Battleship},{ConnectedUsers:Emma},{Stage:Waiting},{Time:19:00}"))) {
            System.out.println(p.type() + ":" + p.data());
        }
    }
}
