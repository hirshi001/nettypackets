package nettypackets.network.listeners;

import io.netty.channel.ChannelHandlerContext;
import nettypackets.network.client.Client;
import nettypackets.packet.Packet;

public class AbstractClientListener implements ClientListener{

    @Override
    public void packetReceived(Packet packet, ChannelHandlerContext context, Client side) {

    }

    @Override
    public void packetWritten(Packet packet, ChannelHandlerContext context, Client side) {

    }

    @Override
    public void disconnected(Client client) {

    }
}
