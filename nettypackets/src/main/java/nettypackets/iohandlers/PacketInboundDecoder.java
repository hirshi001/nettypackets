package nettypackets.iohandlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import nettypackets.network.NetworkSide;
import nettypackets.networkdata.NetworkData;
import nettypackets.networkdata.OutboundPacketStats;
import nettypackets.packet.Packet;

import java.util.List;

public class PacketInboundDecoder<N extends NetworkSide<?>, L extends PacketListener<N>> extends ByteToMessageDecoder {

    public final N networkSide;
    public final OutboundPacketStats stats;

    private final PacketListenerHandler<N, L> packetListener;


    public PacketInboundDecoder(N networkSide){
        this.networkSide = networkSide;
        stats = new OutboundPacketStats();
        packetListener = new PacketListenerHandler<>();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Packet packet;
        int readerIndex = in.readerIndex();
        while ((packet = getPacket(in, ctx)) != null) {
            packet.getPacketHandlerContext().channelHandlerContext = ctx;
            packet.handle(ctx);
            packetListener.packetReceived(packet, ctx, networkSide);
            stats.addPacket(in.readerIndex() - readerIndex);
            readerIndex = in.readerIndex();
        }
    }


    protected Packet getPacket(ByteBuf in, ChannelHandlerContext ctx){
        try {
            return networkSide.getNetworkData().decode(in);
        }catch(Exception e){
            ctx.close();
            e.printStackTrace();
            return null;
        }
    }

    public void addListener(L listener){
        packetListener.addListener(listener);
    }

}
