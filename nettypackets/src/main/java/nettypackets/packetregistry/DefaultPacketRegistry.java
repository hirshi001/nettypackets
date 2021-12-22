package nettypackets.packetregistry;

import nettypackets.packet.Packet;
import nettypackets.packet.PacketHolder;
import nettypackets.util.ByteBufSerializable;
import nettypackets.util.defaultpackets.arraypackets.*;
import nettypackets.util.defaultpackets.objectpackets.ByteBufSerializableObjectPacket;
import nettypackets.util.defaultpackets.objectpackets.ObjectPacket;
import nettypackets.util.defaultpackets.primitivepackets.*;

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
        if(getPacketHolder(id)!=null){
            packetHolderIntMap.values().remove(id);
            classIdMap.values().remove(id);
            intToPacketHolderMap.remove(id);
        }
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

    @Override
    public PacketRegistry registerDefaultPrimitivePackets() {
        register(new PacketHolder<>(BooleanPacket::new, null, BooleanPacket.class), -1);
        register(new PacketHolder<>(BytePacket::new, null, BytePacket.class), -2);
        register(new PacketHolder<>(CharPacket::new, null, CharPacket.class), -3);
        register(new PacketHolder<>(DoublePacket::new, null, DoublePacket.class), -4);
        register(new PacketHolder<>(FloatPacket::new, null, FloatPacket.class), -5);
        register(new PacketHolder<>(IntegerPacket::new, null, IntegerPacket.class), -6);
        register(new PacketHolder<>(LongPacket::new, null, LongPacket.class), -7);
        register(new PacketHolder<>(ShortPacket::new, null, ShortPacket.class), -8);
        register(new PacketHolder<>(StringPacket::new, null, StringPacket.class), -9);
        register(new PacketHolder<>(MultiBooleanPacket::new, null, MultiBooleanPacket.class), -10);
        return this;
    }

    @Override
    public PacketRegistry registerDefaultArrayPrimitivePackets() {
        register(new PacketHolder<>(BooleanArrayPacket::new, null, BooleanArrayPacket.class), -101);
        register(new PacketHolder<>(ByteArrayPacket::new, null, ByteArrayPacket.class), -102);
        register(new PacketHolder<>(CharArrayPacket::new, null, CharArrayPacket.class), -103);
        register(new PacketHolder<>(DoubleArrayPacket::new, null, DoubleArrayPacket.class), -104);
        register(new PacketHolder<>(FloatArrayPacket::new, null, FloatArrayPacket.class), -105);
        register(new PacketHolder<>(IntegerArrayPacket::new, null, IntegerArrayPacket.class), -106);
        register(new PacketHolder<>(LongArrayPacket::new, null, LongArrayPacket.class), -107);
        register(new PacketHolder<>(ShortArrayPacket::new, null, ShortArrayPacket.class), -108);
        return this;
    }

    @Override
    public PacketRegistry registerDefaultObjectPackets() {
        register(new PacketHolder<>(ObjectPacket::new, null, ObjectPacket.class), -201);
        //maybe add ObjectArrayPacket
        return this;
    }
}
