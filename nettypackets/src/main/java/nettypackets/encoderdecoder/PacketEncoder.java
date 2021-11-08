package nettypackets.encoderdecoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import nettypackets.Pair;
import nettypackets.packet.Packet;
import nettypackets.packet.PacketHelper;
import nettypackets.packetregistry.PacketRegistry;

public class PacketEncoder extends MessageToByteEncoder<Pair<PacketRegistry, Packet>> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Pair<PacketRegistry, Packet> msg, ByteBuf out) throws Exception {

        PacketHelper.toBytes(out, msg.a, msg.b);
    }
}
