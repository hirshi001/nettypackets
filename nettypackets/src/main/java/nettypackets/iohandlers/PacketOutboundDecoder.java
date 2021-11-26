package nettypackets.iohandlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import nettypackets.PacketHandlerContext;
import nettypackets.packet.Packet;
import nettypackets.packetdecoderencoder.PacketEncoderDecoder;
import nettypackets.packetregistry.SidedPacketRegistryContainer;
import nettypackets.packet.PacketHelper;

import java.util.List;

public class PacketOutboundDecoder extends ByteToMessageDecoder {

    public final SidedPacketRegistryContainer serverRegistries;

    public PacketEncoderDecoder encoderDecoder;
    private final PacketHandlerContext packetHandlerContext = new PacketHandlerContext();

    public PacketOutboundDecoder(SidedPacketRegistryContainer serverRegistries, PacketEncoderDecoder encoderDecode){
        this.serverRegistries = serverRegistries;
        this.encoderDecoder = encoderDecode;
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Packet packet;
        while((packet = encoderDecoder.decode(serverRegistries, in))!=null){
            packetHandlerContext.channelHandlerContext = ctx;
            packetHandlerContext.sidedPacketRegistryContainer = serverRegistries;
            packetHandlerContext.packetRegistry = packet.packetRegistry;
            packet.handle(packetHandlerContext);
        }
    }

}
