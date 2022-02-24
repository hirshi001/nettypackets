package nettypackets.iohandlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import nettypackets.network.NetworkSide;
import nettypackets.packet.Packet;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.util.tuple.Pair;

public class PacketOutboundEncoder extends MessageToByteEncoder<Pair<Packet, PacketRegistry>> {

    private final NetworkSide networkSide;

    public PacketOutboundEncoder(NetworkSide networkSide){
        this.networkSide = networkSide;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Pair<Packet, PacketRegistry> msg, ByteBuf out) throws Exception {
        try {
            networkSide.getNetworkData().encode(msg.a, msg.b, out);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
