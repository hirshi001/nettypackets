package nettypackets.util;

import nettypackets.packet.Packet;
import nettypackets.packet.PacketHandler;
import nettypackets.packetregistry.PacketRegistry;

public class PacketHandlerContext {

    public PacketHandler<Packet> packetHandler;
    public PacketRegistry packetRegistry;


    public PacketHandlerContext(){

    }

    public PacketHandlerContext(PacketHandlerContext context){
        set(context);
    }

    public void set(PacketHandler<Packet> packetHandler, PacketRegistry packetRegistry){
        this.packetHandler = packetHandler;
        this.packetRegistry = packetRegistry;
    }

    public void set(PacketHandlerContext context){
        this.packetHandler = context.packetHandler;
        this.packetRegistry = context.packetRegistry;
    }





}
