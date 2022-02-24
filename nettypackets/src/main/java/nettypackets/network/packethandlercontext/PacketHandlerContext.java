package nettypackets.network.packethandlercontext;

import io.netty.channel.ChannelHandlerContext;
import nettypackets.network.NetworkSide;
import nettypackets.networkdata.NetworkData;
import nettypackets.packet.Packet;
import nettypackets.packet.PacketHandler;
import nettypackets.packetregistry.PacketRegistry;

import java.net.InetSocketAddress;

public class PacketHandlerContext<T extends Packet> {

    public NetworkData networkData;
    public NetworkSide networkSide;
    public PacketType packetType;
    public PacketRegistry packetRegistry;
    public PacketHandler<T> packetHandler;
    public T packet;
    public ChannelHandlerContext ctx;
    public InetSocketAddress source;

    public PacketHandlerContext(){
    }

    public PacketHandlerContext(NetworkData networkData, NetworkSide networkSide, PacketType packetType, PacketRegistry packetRegistry, PacketHandler<T> packetHandler, T packet, ChannelHandlerContext ctx, InetSocketAddress source){
        this.networkData = networkData;
        this.networkSide = networkSide;
        this.packetType = packetType;
        this.packetRegistry = packetRegistry;
        this.packetHandler = packetHandler;
        this.packet = packet;
        this.ctx = ctx;
        this.source = source;
    }

    public final void handle(){
        packetHandler.handle(this);
    }

}
