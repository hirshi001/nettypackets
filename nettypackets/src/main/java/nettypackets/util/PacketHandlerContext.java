package nettypackets.util;

import io.netty.channel.ChannelHandlerContext;
import nettypackets.packet.Packet;
import nettypackets.packet.PacketHandler;
import nettypackets.packetregistry.PacketRegistry;

public class PacketHandlerContext {

    public PacketHandler<Packet> packetHandler;
    public PacketRegistry packetRegistry;
    public ChannelHandlerContext channelHandlerContext;

    public PacketHandlerContext(){

    }

    public PacketHandlerContext(PacketHandlerContext context){
        set(context);
    }

    public void set(PacketHandler<Packet> packetHandler, PacketRegistry packetRegistry, ChannelHandlerContext channelHandlerContext){
        this.packetHandler = packetHandler;
        this.packetRegistry = packetRegistry;
        this.channelHandlerContext = channelHandlerContext;
    }

    public void set(PacketHandlerContext context){
        this.packetHandler = context.packetHandler;
        this.packetRegistry = context.packetRegistry;
        this.channelHandlerContext = context.channelHandlerContext;
    }





}
