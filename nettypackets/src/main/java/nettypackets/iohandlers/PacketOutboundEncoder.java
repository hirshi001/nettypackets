package nettypackets.iohandlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import nettypackets.network.NetworkSide;
import nettypackets.networkdata.NetworkData;
import nettypackets.packet.Packet;

public class PacketOutboundEncoder<N extends NetworkSide<?>, L extends PacketListener<N>> extends MessageToByteEncoder<Packet> {

    private final N networkSide;
    private final PacketListenerHandler<N,L> packetListener;

    public PacketOutboundEncoder(N networkSide){
        this.networkSide = networkSide;
        packetListener = new PacketListenerHandler<N, L>();

    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
        try {
            msg.getPacketHandlerContext().channelHandlerContext = ctx;
            networkSide.getNetworkData().encode(msg, out);
            packetListener.packetWritten(msg, ctx, networkSide);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void addListener(L listener){
        packetListener.addListener(listener);
    }
}
