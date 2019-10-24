package Lab2_2;

import java.util.HashMap;
import java.util.Map;

public class PacketManager {

    public final static Map<String, Class<? extends Packet>> packets = new HashMap<>();

    static {
        packets.put("ping", GetPP.class);
    }


    public static Packet getPacket(String action) {
        try {
            return packets.get(action).newInstance();
        } catch (IllegalAccessException | InstantiationException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}