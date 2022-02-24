package nettypackets.iohandlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import nettypackets.network.NetworkSide;
import nettypackets.network.packethandlercontext.PacketHandlerContext;
import nettypackets.network.packethandlercontext.PacketType;

import java.util.List;

public class PacketInboundDecoder extends ByteToMessageDecoder {

    public final NetworkSide networkSide;
    public PacketInboundDecoder(NetworkSide networkSide){
        this.networkSide = networkSide;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        PacketHandlerContext packetHandlerContext;
        while ((packetHandlerContext = getPacket(in, ctx)) != null) {
            packetHandlerContext.networkSide = networkSide;
            packetHandlerContext.ctx = ctx;
            packetHandlerContext.packetType = PacketType.TCP;
            out.add(packetHandlerContext);
        }
    }

    protected PacketHandlerContext getPacket(ByteBuf in, ChannelHandlerContext ctx){
        try {
            return networkSide.getNetworkData().decode(in);
        }catch(Exception e){
            ctx.close();
            e.printStackTrace();
            return null;
        }
    }
}
