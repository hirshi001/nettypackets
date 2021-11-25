package nettypackets;

import io.netty.channel.ChannelHandlerContext;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.packetregistry.SidedPacketRegistryContainer;

public class PacketHandlerContext {

    public ChannelHandlerContext channelHandlerContext;
    public PacketRegistry packetRegistry;
    public SidedPacketRegistryContainer sidedPacketRegistryContainer;

    //1 setter method for all variables
    public void set(ChannelHandlerContext channelHandlerContext, PacketRegistry packetRegistry, SidedPacketRegistryContainer sidedPacketRegistryContainer) {
        this.channelHandlerContext = channelHandlerContext;
        this.packetRegistry = packetRegistry;
        this.sidedPacketRegistryContainer = sidedPacketRegistryContainer;
    }




}
