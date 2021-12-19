package nettypackets.network.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.Future;
import nettypackets.network.PacketResponseFuture;
import nettypackets.networkdata.NetworkData;
import nettypackets.packet.Packet;

public class DefaultServer implements Server{


    private final int port;
    private final NetworkData networkData;
    public final ChannelGroup channels;
    EventLoopGroup worker, boss;
    private boolean connected;


    public DefaultServer(int port, NetworkData networkData, ChannelGroup channels) {
        this.port = port;
        this.networkData = networkData;
        this.channels = channels;
    }

    @Override
    public NetworkData getNetworkData() {
        return networkData;
    }

    @Override
    public boolean isClient() {
        return false;
    }

    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    public ChannelGroupFuture sendPacketToAll(Packet packet) {
        return channels.writeAndFlush(packet);
    }

    @Override
    public ChannelFuture sendPacket(Packet packet, Channel channel) {
        return channel.writeAndFlush(packet);
    }

    @Override
    public PacketResponseFuture sendPacketWithResponse(Packet packet, Channel channel, long timeout) {
        //PacketResponseFuture packetResponseFuture = new PacketResponseFuture(packet, channel, timeout);
        return null;
    }

    @Override
    public ChannelFuture connect(ServerBootstrap bootstrap) {
        boss = bootstrap.config().group();
        worker = bootstrap.config().childGroup();
        return bootstrap.bind(port).addListener((ChannelFutureListener) future -> {
            if(future.isSuccess()) connected = true;
            else connected = false;
        });
    }

    @Override
    public Future<?> disconnect() {
        worker.shutdownGracefully();
        return boss.shutdownGracefully().addListener( future -> {if(future.isSuccess()) connected = false;});
    }

    @Override
    public boolean isConnected() {
        return connected;
    }


}
