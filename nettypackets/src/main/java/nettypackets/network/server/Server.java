package nettypackets.network.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.util.concurrent.Future;
import nettypackets.network.NetworkSide;
import nettypackets.network.PacketResponseFuture;
import nettypackets.packet.Packet;

public interface Server extends NetworkSide<ServerBootstrap> {

    public ChannelGroupFuture sendPacketToAll(Packet packet);

    public ChannelFuture sendPacket(Packet packet, Channel channel);

    public PacketResponseFuture sendPacketWithResponse(Packet packet, Channel channel, long timeout);

    public ChannelFuture connect(ServerBootstrap bootstrap);

    public Future<?> disconnect();

    public boolean isConnected();

}
