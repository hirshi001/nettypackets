package nettypackets.network.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.Future;
import nettypackets.iohandlers.PacketInboundDecoder;
import nettypackets.iohandlers.PacketOutboundEncoder;
import nettypackets.network.PacketResponseManager;
import nettypackets.network.listeners.AbstractServerListener;
import nettypackets.network.listeners.ServerListener;
import nettypackets.network.listeners.ServerListenerHandler;
import nettypackets.networkdata.NetworkData;
import nettypackets.packet.Packet;
import nettypackets.restapi.RestAction;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefaultServer implements Server{


    private final int port;
    private final NetworkData networkData;
    public final ChannelGroup channels;
    EventLoopGroup worker, boss;
    private boolean connected;

    private final PacketResponseManager packetResponseManager;

    private final ServerListenerHandler listenerHandler;

    public DefaultServer(int port, NetworkData networkData, ChannelGroup channels) {
        this.port = port;
        this.networkData = networkData;
        this.channels = channels;
        packetResponseManager = new PacketResponseManager(new DefaultEventExecutor(), new ScheduledThreadPoolExecutor(1));
        listenerHandler = new ServerListenerHandler();
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
        packetResponseManager.noId(packet);
        return channels.writeAndFlush(packet);
    }

    @Override
    public ChannelFuture sendPacket(Packet packet, Channel channel) {
        packetResponseManager.noId(packet);
        return channel.writeAndFlush(packet);
    }

    @Override
    public RestAction<Packet> sendPacketWithResponse(Packet packet, Channel channel, long timeout) {
        return packetResponseManager.submit(packet, channel, timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public ChannelFuture connect(ServerBootstrap bootstrap) {

        //ServerPacketDecoder decoder = new ServerPacketDecoder(this, );
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {


                PacketOutboundEncoder<Server, ServerListener> packetOutboundEncoder = new PacketOutboundEncoder<>(DefaultServer.this);
                PacketInboundDecoder<Server, ServerListener> packetInboundDecoder = new PacketInboundDecoder<Server, ServerListener>(DefaultServer.this){
                    @Override
                    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                        super.channelRegistered(ctx);
                        DefaultServer.this.channels.add(ctx.channel());
                        listenerHandler.clientConnected(DefaultServer.this, ctx);
                    }

                    @Override
                    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
                        super.channelUnregistered(ctx);
                        DefaultServer.this.channels.remove(ctx.channel());
                        listenerHandler.clientDisconnected(DefaultServer.this, ctx);
                    }
                };

                packetInboundDecoder.addListener(listenerHandler); //only to listen to when packets are sent/received
                packetInboundDecoder.addListener(new AbstractServerListener() {
                    @Override
                    public void packetReceived(Packet packet, ChannelHandlerContext context, Server side) {
                        packetResponseManager.success(packet);
                    }
                }); //when packet received, handle the response

                packetOutboundEncoder.addListener(listenerHandler); //only to listen to when packets are sent/received

                ch.pipeline().addLast(packetOutboundEncoder, packetInboundDecoder);
            }
        });

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
        return boss.shutdownGracefully().addListener( future -> {
            if(future.isSuccess()){
                connected = false;
                listenerHandler.disconnected(this);
            }
        });
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void addListener(ServerListener listener) {
        listenerHandler.addListener(listener);
    }

}
