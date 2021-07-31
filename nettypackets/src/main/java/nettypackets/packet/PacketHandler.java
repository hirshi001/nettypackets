package nettypackets.packet;

import io.netty.channel.ChannelHandlerContext;

public interface PacketHandler<T extends Packet> {

    public void handle(T packet, ChannelHandlerContext ctx);

}
