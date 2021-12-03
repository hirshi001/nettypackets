package nettypackets.networkdata;

public class OutboundPacketStats {

    public int packetsSent;
    public int bytesSent;

    public OutboundPacketStats() {
        packetsSent = 0;
        bytesSent = 0;
    }

    public void addPacket(int size) {
        packetsSent++;
        bytesSent += size;
    }

    public void reset() {
        packetsSent = 0;
        bytesSent = 0;
    }

    @Override
    public String toString() {
        return "Outbound Packets: " + packetsSent + " - Bytes: " + bytesSent;
    }

}
