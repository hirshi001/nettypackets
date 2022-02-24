package nettypackets.network.listeners.clientlistener;

import io.netty.channel.ChannelHandlerContext;
import nettypackets.network.client.IClient;
import nettypackets.network.packethandlercontext.PacketHandlerContext;
import nettypackets.packet.Packet;
import nettypackets.packetregistry.PacketRegistry;

public class AbstractClientListener<C extends IClient> implements ClientListener<C>{


    @Override
    public void tcpPacketReceived(PacketHandlerContext<?> context, C side) {

    }

    @Override
    public void udpPacketReceived(PacketHandlerContext<?> context, C side) {

    }

    @Override
    public void udpPacketWritten(Packet packet, PacketRegistry registry, ChannelHandlerContext context, C side) {

    }

    @Override
    public void tcpPacketWritten(Packet packet, PacketRegistry registry, ChannelHandlerContext context, C side) {

    }

    @Override
    public void connectedUDP(C client) {

    }

    @Override
    public void disconnectedUDP(C client) {

    }

    @Override
    public void connectedTCP(C client) {

    }

    @Override
    public void disconnectedTCP(C client) {

    }
}
