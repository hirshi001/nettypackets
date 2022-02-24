package nettypackets.iohandlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import nettypackets.network.NetworkSide;
import nettypackets.packet.Packet;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.util.tuple.Triple;

import java.net.InetSocketAddress;
import java.util.List;

public class DatagramPacketOutboundEncoder extends MessageToMessageEncoder<Triple<Packet, PacketRegistry, InetSocketAddress>> {

    private NetworkSide side;

    public DatagramPacketOutboundEncoder(NetworkSide side) {
        this.side = side;
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, Triple<Packet, PacketRegistry, InetSocketAddress> msg, List<Object> out) throws Exception {
        ByteBuf buf = Unpooled.buffer();
        side.getNetworkData().encode(msg.a, msg.b, buf);
        out.add(new DatagramPacket(buf, msg.c));
    }
}
