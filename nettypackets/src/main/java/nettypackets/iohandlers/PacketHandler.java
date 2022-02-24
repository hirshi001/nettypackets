package nettypackets.iohandlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import nettypackets.network.packethandlercontext.PacketHandlerContext;

public class PacketHandler extends SimpleChannelInboundHandler<PacketHandlerContext<?>> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PacketHandlerContext<?> msg) throws Exception {
        msg.handle();
    }
}
