package nettypackets.iohandlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import nettypackets.Pair;
import nettypackets.packet.Packet;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.packetregistry.SidedPacketRegistryContainer;

public class PacketInboundEncoder extends MessageToByteEncoder<Pair<PacketRegistry, Packet>> {

    private final SidedPacketRegistryContainer container;

    public PacketInboundEncoder(SidedPacketRegistryContainer container){
        this.container = container;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Pair<PacketRegistry, Packet> msg, ByteBuf out) throws Exception {
        container.packetEncoderDecoder.encode(msg.b, msg.a, out);
    }
}
