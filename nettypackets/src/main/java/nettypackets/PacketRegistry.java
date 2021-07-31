package nettypackets;

import nettypackets.packet.Packet;
import nettypackets.packet.PacketHolder;

import java.util.HashMap;
import java.util.Map;

public class PacketRegistry {

    protected final Map<Class<? extends Packet>, Integer> classIdMap;
    protected final Map<Integer, PacketHolder<?>> intToPacketHolderMap;
    protected final Map<PacketHolder<?>, Integer> packetHolderIntMap;
    public final String registryName;

    public PacketRegistry(String registryName){
        this.classIdMap = new HashMap<>();
        this.intToPacketHolderMap = new HashMap<>();
        this.packetHolderIntMap = new HashMap<>();
        this.registryName = registryName;
    }

    public final void register(PacketHolder<?> packetHolder, int id){
        classIdMap.put(packetHolder.packetClass, id);
        intToPacketHolderMap.put(id, packetHolder);
        packetHolderIntMap.put(packetHolder, id);
    }

    public final PacketHolder<?> getPacketHolder(int id){
        return intToPacketHolderMap.get(id);
    }

    public final int getId(PacketHolder<?> holder){
        return packetHolderIntMap.get(holder);
    }

    public final int getId(Class<? extends Packet> clazz){
        return classIdMap.get(clazz);
    }

}
