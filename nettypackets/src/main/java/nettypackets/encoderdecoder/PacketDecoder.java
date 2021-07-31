package nettypackets.encoderdecoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import nettypackets.SidedPacketRegistryContainer;
import nettypackets.packet.PacketHelper;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {


    private final SidedPacketRegistryContainer serverRegistries;

    public PacketDecoder(SidedPacketRegistryContainer serverRegistries){
        this.serverRegistries = serverRegistries;
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        PacketHelper.handle(in, ctx, serverRegistries);
    }

}
