package nettypackets.network.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import nettypackets.iohandlers.PacketInboundDecoder;
import nettypackets.iohandlers.PacketOutboundEncoder;
import nettypackets.network.PacketResponseFuture;
import nettypackets.network.listeners.AbstractClientListener;
import nettypackets.network.listeners.ClientListener;
import nettypackets.network.listeners.ClientListenerHandler;
import nettypackets.networkdata.NetworkData;
import nettypackets.packet.Packet;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultClient implements Client{

    private final int port;
    private final String host;
    private final NetworkData networkData;
    private EventLoopGroup workerGroup;
    private volatile boolean connected;

    public ChannelHandlerContext channel;
    private final ClientListenerHandler listenerHandler;

    private final AtomicInteger packetResponseId;
    private final Map<Integer, PacketResponseFuture> packetResponses;

    private final EventExecutor executor;
    private final ScheduledExecutorService scheduledExecutorService;

    public DefaultClient(String host, int port, NetworkData networkData) {
        this.port = port;
        this.host = host;
        this.networkData = networkData;
        this.listenerHandler = new ClientListenerHandler();
        packetResponseId = new AtomicInteger(0);
        packetResponses = new ConcurrentHashMap<>();
        executor = new DefaultEventExecutor();
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
    }

    public ChannelFuture sendPacket(Packet packet) {
        packet.clientId = -1; //make sure there are no collisions
        return channel.channel().writeAndFlush(packet);
    }

    @Override
    public PacketResponseFuture sendPacketWithResponse(Packet packet, long timeout) {
        int id = getNextPacketResponseId();

        PacketResponseFuture responseFuture = new PacketResponseFuture(id, executor);
        packet.clientId = id;
        packetResponses.put(id, responseFuture);
        channel.channel().writeAndFlush(packet);

        scheduledExecutorService.schedule(()-> {
            packetResponses.remove(id).setFailure(new TimeoutException("Packet did not arrive"));
        }, timeout, TimeUnit.MILLISECONDS);

        return responseFuture;
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

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                PacketOutboundEncoder<Client, ClientListener> packetOutboundEncoder = new PacketOutboundEncoder<>(DefaultClient.this);
                PacketInboundDecoder<Client, ClientListener> packetInboundDecoder = new PacketInboundDecoder<Client, ClientListener>(DefaultClient.this){
                    @Override
                    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                        super.channelRegistered(ctx);
                        DefaultClient.this.channel = ctx;
                    }
                };

                packetInboundDecoder.addListener(listenerHandler);
                packetInboundDecoder.addListener(new AbstractClientListener() {
                    @Override
                    public void packetReceived(Packet packet, ChannelHandlerContext context, Client side) {
                        if(packetResponses.containsKey(packet.clientId)){
                            packetResponses.remove(packet.clientId).setSuccess(packet);
                        }
                    }
                });

                packetOutboundEncoder.addListener(listenerHandler);

                ch.pipeline().addLast(packetOutboundEncoder, packetInboundDecoder);
            }
        });

        workerGroup = bootstrap.config().group();
        return bootstrap.connect(host, port).addListener(future -> {
            if(future.isSuccess()) connected = true;
        });
    }

    @Override
    public Future<?> disconnect() {
        listenerHandler.disconnected(this);
        return workerGroup.shutdownGracefully();
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public boolean addListener(ClientListener listener) {
        return listenerHandler.addListener(listener);
    }

    @Override
    public boolean removeListener(ClientListener listener) {
        return listenerHandler.addListener(listener);
    }

    public ChannelHandlerContext getChannel() {
        return channel;
    }

    private int getNextPacketResponseId(){
        return packetResponseId.getAndIncrement();
    }
}
