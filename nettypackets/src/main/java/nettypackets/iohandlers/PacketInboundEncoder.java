package nettypackets.iohandlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import nettypackets.Pair;
import nettypackets.packet.Packet;
import nettypackets.packetdecoderencoder.PacketEncoderDecoder;
import nettypackets.packetregistry.PacketRegistry;

public class PacketInboundEncoder extends MessageToByteEncoder<Pair<PacketRegistry, Packet>> {

    private final PacketEncoderDecoder encoderDecoder;

    public PacketInboundEncoder(PacketEncoderDecoder encoderDecoder){
        this.encoderDecoder = encoderDecoder;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Pair<PacketRegistry, Packet> msg, ByteBuf out) throws Exception {
        encoderDecoder.encode(msg.b, msg.a, out);
    }
}
