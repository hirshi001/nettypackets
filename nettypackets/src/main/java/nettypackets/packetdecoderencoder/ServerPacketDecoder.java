package nettypackets.packetdecoderencoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import nettypackets.iohandlers.PacketInboundDecoder;
import nettypackets.network.listeners.ServerListener;
import nettypackets.network.server.Server;
import nettypackets.networkdata.NetworkData;

public class ServerPacketDecoder extends PacketInboundDecoder<Server, ServerListener> {

    ChannelGroup group;

    public ServerPacketDecoder(Server server, ChannelGroup group) {
        super(server);
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
