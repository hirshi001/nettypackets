package nettypackets;

import io.netty.channel.ChannelHandlerContext;
import nettypackets.packet.Packet;
import nettypackets.packet.PacketHandler;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.packetregistry.SidedPacketRegistryContainer;

public class PacketHandlerContext {

    public PacketHandler<Packet> packetHandler;
    public PacketRegistry packetRegistry;
    public SidedPacketRegistryContainer sidedPacketRegistryContainer;


    public PacketHandlerContext(){

    }

    public PacketHandlerContext(PacketHandlerContext context){
        set(context);
    }

    //1 setter method for all variables
    public void set(PacketHandler<Packet> packetHandler, PacketRegistry packetRegistry, SidedPacketRegistryContainer sidedPacketRegistryContainer) {
        this.packetHandler = packetHandler;
        this.packetRegistry = packetRegistry;
        this.sidedPacketRegistryContainer = sidedPacketRegistryContainer;
    }

    public void set(PacketHandlerContext context){
        this.packetHandler = context.packetHandler;
        this.packetRegistry = context.packetRegistry;
        this.sidedPacketRegistryContainer = context.sidedPacketRegistryContainer;
    }





}
