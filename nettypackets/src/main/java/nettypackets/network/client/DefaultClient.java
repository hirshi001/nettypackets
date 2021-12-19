package nettypackets.network.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.Future;
import nettypackets.network.PacketResponseFuture;
import nettypackets.networkdata.NetworkData;
import nettypackets.packet.Packet;

import java.util.function.Supplier;

public class DefaultClient implements Client{

    private final int port;
    private final String host;
    private final NetworkData networkData;
    private EventLoopGroup workerGroup;
    private volatile boolean connected;

    public final Supplier<ChannelHandlerContext> channel;

    public DefaultClient(String host, int port, NetworkData networkData, Supplier<ChannelHandlerContext> channel) {
        this.port = port;
        this.host = host;
        this.networkData = networkData;
        this.channel = channel;
    }

    public ChannelFuture sendPacket(Packet packet) {
        return channel.get().channel().writeAndFlush(packet);
    }

    @Override
    public PacketResponseFuture sendPacketWithResponse(Packet packet, long timeout) {
        channel.get().channel().writeAndFlush(packet);
        return null;
    }

    @Override
    public NetworkData getNetworkData() {
        return networkData;
    }

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public boolean isServer() {
        return false;
    }

    @Override
    public ChannelFuture connect(Bootstrap bootstrap) {
        workerGroup = bootstrap.config().group();
        return bootstrap.connect(host, port).addListener(future -> {
            if(future.isSuccess()) connected = true;
        });
    }

    @Override
    public Future<?> disconnect() {
        return workerGroup.shutdownGracefully();
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

}
