package nettypackets.iohandlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import nettypackets.networkdata.NetworkData;
import nettypackets.networkdata.OutboundPacketStats;
import nettypackets.packet.Packet;

import java.util.List;

public class PacketOutboundDecoder extends ByteToMessageDecoder {

    public final NetworkData networkData;
    public final OutboundPacketStats stats;
    public ChannelHandlerContext channel;


    public PacketOutboundDecoder(NetworkData networkData){
        this.networkData = networkData;
        stats =  new OutboundPacketStats();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Packet packet;
        int readerIndex = in.readerIndex();
        while ((packet = getPacket(in, ctx)) != null) {
            packet.handle(ctx);
            stats.addPacket(in.readerIndex() - readerIndex);
            readerIndex = in.readerIndex();
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx;

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        this.channel = null;
    }

    protected Packet getPacket(ByteBuf in, ChannelHandlerContext ctx){
        try {
            return networkData.decode(in);
        }catch(Exception e){
            ctx.close();
            e.printStackTrace();
            return null;
        }
    }

}
