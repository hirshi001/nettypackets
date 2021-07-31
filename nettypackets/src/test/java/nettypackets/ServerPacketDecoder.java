package nettypackets;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import nettypackets.encoderdecoder.PacketDecoder;

public class ServerPacketDecoder extends PacketDecoder {

    ChannelGroup group;

    public ServerPacketDecoder(SidedPacketRegistryContainer serverRegistries, ChannelGroup group) {
        super(serverRegistries);
        this.group = group;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        group.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        group.remove(ctx.channel());
    }
}
