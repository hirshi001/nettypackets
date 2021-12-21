package nettypackets.network.listeners;

import io.netty.channel.ChannelHandlerContext;
import nettypackets.network.server.Server;
import nettypackets.packet.Packet;

public class AbstractServerListener implements ServerListener{
    @Override
    public void packetReceived(Packet packet, ChannelHandlerContext context, Server side) {

    }

    @Override
    public void packetWritten(Packet packet, ChannelHandlerContext context, Server side) {

    }

    @Override
    public void clientDisconnected(Server server, ChannelHandlerContext context) {

    }

    @Override
    public void disconnected(Server server) {

    }

    @Override
    public void clientConnected(Server server, ChannelHandlerContext context) {

    }
}
