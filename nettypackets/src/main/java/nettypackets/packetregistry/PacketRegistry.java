package nettypackets.packetregistry;

import nettypackets.packet.Packet;
import nettypackets.packet.PacketHolder;

public interface PacketRegistry {

    public PacketRegistry register(PacketHolder<?> packetHolder, int id);

    public PacketHolder<?> getPacketHolder(int id);

    public int getId(PacketHolder<?> holder);

    public int getId(Class<? extends Packet> clazz);

    public String getRegistryName();

}
