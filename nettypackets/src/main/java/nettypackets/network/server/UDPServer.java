package nettypackets.network.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.DatagramChannel;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import nettypackets.iohandlers.DatagramPacketInboundDecoder;
import nettypackets.iohandlers.DatagramPacketOutboundEncoder;
import nettypackets.iohandlers.PacketHandler;
import nettypackets.network.packethandlercontext.PacketHandlerContext;
import nettypackets.network.packethandlercontext.PacketType;
import nettypackets.networkdata.NetworkData;
import nettypackets.packet.Packet;
import nettypackets.packet.PacketHolder;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.packetregistrycontainer.PacketRegistryContainer;
import nettypackets.restapi.RestAction;
import nettypackets.util.defaultpackets.primitivepackets.BooleanPacket;
import nettypackets.util.defaultpackets.udppackets.UDPInitialConnectionPacket;
import nettypackets.util.tuple.Triple;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class UDPServer extends AbstractServer<UDPServer> {

    protected EventLoopGroup udpEventLoopGroup;
    protected boolean connected;
    protected Channel channel;

    protected final Set<InetSocketAddress> connections = ConcurrentHashMap.newKeySet(), connectionsView = Collections.unmodifiableSet(connections);

    public UDPServer(int port, NetworkData networkData, @Nullable EventExecutor eventExecutor) {
        this(port, networkData, eventExecutor, null);

    }

    public UDPServer(int port, NetworkData networkData, @Nullable EventExecutor eventExecutor, IServer parentSide) {
        super(port, networkData, eventExecutor, PacketType.UDP, parentSide);

        PacketRegistry defaultRegistry = getNetworkData().getPacketRegistryContainer().getDefaultRegistry().registerUDPHelperPackets();
        PacketHolder<UDPInitialConnectionPacket> holder = (PacketHolder<UDPInitialConnectionPacket>) defaultRegistry.getPacketHolder(defaultRegistry.getId(UDPInitialConnectionPacket.class));
        holder.handler = context -> {
            connections.add(context.source);
            send(new BooleanPacket(true).setResponsePacket(context.packet),
                    context.packetRegistry,
                    context.source);
        };
        getNetworkData().getPacketRegistryContainer().getDefaultRegistry().registerUDPHelperPackets();
    }


    @Override
    public Future<?> disconnect() {
        return udpEventLoopGroup.shutdownGracefully();
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    public ChannelFuture connect(Bootstrap bootstrap) {
        bootstrap.handler(new ChannelInitializer<DatagramChannel>() {
            @Override
            protected void initChannel(DatagramChannel ch) throws Exception {
                DatagramPacketOutboundEncoder packetOutboundEncoder = new DatagramPacketOutboundEncoder(parentOrThis()){
                    @Override
                    protected void encode(ChannelHandlerContext ctx, Triple<Packet, PacketRegistry, InetSocketAddress> msg, List<Object> out) throws Exception {
                        super.encode(ctx, msg, out);
                        listenerHandler.udpPacketWritten(msg.a, msg.b, ctx, parentOrThis());
                    }
                };

                DatagramPacketInboundDecoder packetInboundDecoder = new DatagramPacketInboundDecoder(parentOrThis());


                PacketHandler packetHandler = new PacketHandler(){
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, PacketHandlerContext<?> msg) throws Exception {
                        super.channelRead0(ctx, msg);
                        packetResponseManager.success(msg);
                        listenerHandler.udpPacketReceived(msg, parentOrThis());
                    }

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        super.channelActive(ctx);
                        listenerHandler.udpClientConnected(parentOrThis(), ctx);
                    }

                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                        super.channelInactive(ctx);
                        listenerHandler.udpClientDisconnected(parentOrThis(), ctx);
                    }
                };

                ch.pipeline().addLast(packetOutboundEncoder, packetInboundDecoder, packetHandler);
            }
        });
        udpEventLoopGroup = bootstrap.config().group();
        return bootstrap.bind(port).addListener((ChannelFutureListener) future -> {
                listenerHandler.udpConnected(parentOrThis());
                connected = true;
                channel = future.channel();
        });
    }

    public ChannelFuture send(Packet packet, PacketRegistry registry, InetSocketAddress address) {
        packetResponseManager.noId(packet);
        return channel.writeAndFlush(new Triple<>(packet, registry, address));
    }

    public void sendToAll(Packet packet, PacketRegistry registry){
        for (InetSocketAddress address : connections) {
            send(packet, registry, address);
        }
    }

    public RestAction<PacketHandlerContext<?>> sendWithResponse(Packet packet, PacketRegistry registry, InetSocketAddress address, long timeout) {
        return packetResponseManager.submit(packet, ()-> channel.writeAndFlush(new Triple<>(packet, registry, address)), timeout, TimeUnit.MILLISECONDS);
    }

    public Set<InetSocketAddress> getConnections() {
        return connectionsView;
    }


}
