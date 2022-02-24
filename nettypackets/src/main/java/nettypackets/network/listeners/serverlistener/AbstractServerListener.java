package nettypackets.network.listeners.serverlistener;

import io.netty.channel.ChannelHandlerContext;
import nettypackets.network.packethandlercontext.PacketHandlerContext;
import nettypackets.network.server.IServer;
import nettypackets.packet.Packet;
import nettypackets.packetregistry.PacketRegistry;

public class AbstractServerListener<S extends IServer<S>> implements ServerListener<S>{

    @Override
    public void tcpPacketReceived(PacketHandlerContext<?> context, S side) {

    }

    @Override
    public void udpPacketReceived(PacketHandlerContext<?> context, S side) {

    }

    @Override
    public void udpPacketWritten(Packet packet, PacketRegistry registry, ChannelHandlerContext context, S side) {

    }

    @Override
    public void tcpPacketWritten(Packet packet, PacketRegistry registry, ChannelHandlerContext context, S side) {

    }

    @Override
    public void tcpClientDisconnected(S server, ChannelHandlerContext context) {

    }

    @Override
    public void tcpClientConnected(S server, ChannelHandlerContext context) {

    }

    @Override
    public void udpClientDisconnected(S server, ChannelHandlerContext context) {

    }

    @Override
    public void udpClientConnected(S server, ChannelHandlerContext context) {

    }

    @Override
    public void udpConnected(S server) {

    }

    @Override
    public void tcpConnected(S server) {

    }

    @Override
    public void udpDisconnected(S server) {

    }

    @Override
    public void tcpDisconnected(S server) {

    }
}
