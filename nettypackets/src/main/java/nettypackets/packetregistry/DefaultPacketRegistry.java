package nettypackets.packetregistry;

import nettypackets.packet.Packet;
import nettypackets.packet.PacketHolder;

import java.util.HashMap;
import java.util.Map;

public class DefaultPacketRegistry implements PacketRegistry{

    protected final Map<Class<? extends Packet>, Integer> classIdMap;
    protected final Map<Integer, PacketHolder<?>> intToPacketHolderMap;
    protected final Map<PacketHolder<?>, Integer> packetHolderIntMap;
    public final String registryName;

    public DefaultPacketRegistry(String registryName){
        this.classIdMap = new HashMap<>();
        this.intToPacketHolderMap = new HashMap<>();
        this.packetHolderIntMap = new HashMap<>();
        this.registryName = registryName;
    }

    @Override
    public final PacketRegistry register(PacketHolder<?> packetHolder, int id){
        classIdMap.put(packetHolder.packetClass, id);
        intToPacketHolderMap.put(id, packetHolder);
        packetHolderIntMap.put(packetHolder, id);
        return this;
    }

    @Override
    public final PacketHolder<?> getPacketHolder(int id){
        return intToPacketHolderMap.get(id);
    }

    @Override
    public final int getId(PacketHolder<?> holder){
        return packetHolderIntMap.get(holder);
    }

    @Override
    public final int getId(Class<? extends Packet> clazz){
        return classIdMap.get(clazz);
    }

    @Override
    public final String getRegistryName(){
        return registryName;
    }


}
