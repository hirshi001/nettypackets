package nettypackets.iohandlers;

import io.netty.channel.ChannelHandlerContext;
import nettypackets.network.NetworkSide;
import nettypackets.packet.Packet;

public interface PacketListener<T extends NetworkSide<?>> {

    public void packetReceived(Packet packet, ChannelHandlerContext context, T side);

    public void packetWritten(Packet packet, ChannelHandlerContext context, T side);

}
