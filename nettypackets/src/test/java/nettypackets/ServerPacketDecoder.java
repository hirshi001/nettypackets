package nettypackets;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import nettypackets.iohandlers.PacketOutboundDecoder;
import nettypackets.networkdata.NetworkData;

public class ServerPacketDecoder extends PacketOutboundDecoder {

    ChannelGroup group;

    public ServerPacketDecoder(NetworkData networkData, ChannelGroup group) {
        super(networkData);
        this.group = group;

    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        group.add(ctx.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        group.remove(ctx.channel());
    }
}
