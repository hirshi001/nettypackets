package nettypackets.packet;

import io.netty.channel.ChannelHandlerContext;
import nettypackets.PacketHandlerContext;
import nettypackets.packetregistry.PacketRegistry;

public interface PacketHandler<T extends Packet> {

    public void handle(T packet, PacketHandlerContext ctx);

}
