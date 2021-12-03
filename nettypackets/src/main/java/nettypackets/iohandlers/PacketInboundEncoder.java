package nettypackets.iohandlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import nettypackets.networkdata.NetworkData;
import nettypackets.packet.Packet;

public class PacketInboundEncoder extends MessageToByteEncoder<Packet> {

    private final NetworkData networkData;

    public PacketInboundEncoder(NetworkData networkData){
        this.networkData = networkData;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
        try {
            networkData.encode(msg, out);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
