package nettypackets.network.listeners;

import io.netty.channel.ChannelHandlerContext;
import nettypackets.network.NetworkSide;
import nettypackets.network.packethandlercontext.PacketHandlerContext;
import nettypackets.packet.Packet;
import nettypackets.packetregistry.PacketRegistry;

public interface PacketListener<S extends NetworkSide> {

    public void tcpPacketReceived(PacketHandlerContext<?> context, S side);

    public void udpPacketReceived(PacketHandlerContext<?> context, S side);

    public void udpPacketWritten(Packet packet, PacketRegistry registry, ChannelHandlerContext context, S side);

    public void tcpPacketWritten(Packet packet, PacketRegistry registry, ChannelHandlerContext context, S side);

}
