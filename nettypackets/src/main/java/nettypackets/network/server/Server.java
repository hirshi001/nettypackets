package nettypackets.network.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.util.concurrent.Future;
import nettypackets.network.NetworkSide;
import nettypackets.network.listeners.ServerListener;
import nettypackets.packet.Packet;
import nettypackets.restapi.RestAction;

public interface Server extends NetworkSide<ServerBootstrap> {

    public ChannelGroupFuture sendPacketToAll(Packet packet);

    public ChannelFuture sendPacket(Packet packet, Channel channel);

    public RestAction<Packet> sendPacketWithResponse(Packet packet, Channel channel, long timeout);

    public ChannelFuture connect(ServerBootstrap bootstrap);

    public Future<?> disconnect();

    public boolean isConnected();

    public void addListener(ServerListener listener);

}
