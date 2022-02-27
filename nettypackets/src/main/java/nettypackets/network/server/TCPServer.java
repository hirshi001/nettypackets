package nettypackets.network.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import nettypackets.iohandlers.PacketHandler;
import nettypackets.iohandlers.PacketInboundDecoder;
import nettypackets.iohandlers.PacketOutboundEncoder;
import nettypackets.network.packethandlercontext.PacketHandlerContext;
import nettypackets.network.packethandlercontext.PacketType;
import nettypackets.networkdata.NetworkData;
import nettypackets.packet.Packet;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.restapi.RestAction;
import nettypackets.util.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

public class TCPServer extends AbstractServer<TCPServer>  {

    protected EventLoopGroup tcpWorker, tcpBoss;

    private boolean tcpConnected;
    private ChannelGroup channels;

    public TCPServer(int port, NetworkData networkData, @Nullable EventExecutor eventExecutor) {
        this(port, networkData, eventExecutor, null);
    }



    public TCPServer(int port, NetworkData networkData, @Nullable EventExecutor eventExecutor, IServer parentSide) {
        super(port, networkData, eventExecutor, PacketType.TCP, parentSide);
        channels = new DefaultChannelGroup(this.eventExecutor);
    }
    @Override
    public Future<?> disconnect() {
        if(tcpBoss != null && tcpWorker != null) {
            tcpBoss.shutdownGracefully();
            return tcpWorker.shutdownGracefully().addListener(f -> tcpConnected = f.isSuccess());
        }
        throw new IllegalStateException("TCP Server is not connected");
    }

    @Override
    public boolean isConnected() {
        return tcpConnected;
    }

    public ChannelFuture connect(ServerBootstrap bootstrap) {
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                PacketOutboundEncoder packetOutboundEncoder = new PacketOutboundEncoder(parentOrThis()){
                    @Override
                    protected void encode(ChannelHandlerContext ctx, Pair<Packet, PacketRegistry> msg, ByteBuf out) throws Exception {
                        super.encode(ctx, msg, out);
                        listenerHandler.tcpPacketWritten(msg.a, msg.b, ctx, parentOrThis());
                    }
                };
                PacketInboundDecoder packetInboundDecoder = new PacketInboundDecoder(parentOrThis()) {
                    @Override
                    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                        super.channelRegistered(ctx);
                        TCPServer.this.channels.add(ctx.channel());
                        listenerHandler.tcpClientConnected(parentOrThis(), ctx);
                    }

                    @Override
                    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
                        super.channelUnregistered(ctx);
                        TCPServer.this.channels.remove(ctx.channel());
                        listenerHandler.tcpClientDisconnected(parentOrThis(), ctx);
                    }
                };

                PacketHandler packetHandler = new PacketHandler(){
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, PacketHandlerContext<?> msg) throws Exception {
                        super.channelRead0(ctx, msg);
                        listenerHandler.tcpPacketReceived(msg, parentOrThis());
                        packetResponseManager.success(msg);
                    }
                };

                ch.pipeline().addLast(packetOutboundEncoder, packetInboundDecoder, packetHandler);
            }
        });
        tcpBoss = bootstrap.config().group();
        tcpWorker = bootstrap.config().childGroup();
        return bootstrap.bind(port).addListener((ChannelFutureListener) future -> {
            listenerHandler.tcpConnected(parentOrThis());
            tcpConnected = future.isSuccess();
        });
    }

    public ChannelGroupFuture sendToAll(Packet packet, PacketRegistry registry) {
        packetResponseManager.noId(packet);
        return channels.writeAndFlush(new Pair<>(packet, registry));
    }

    public ChannelFuture send(Packet packet, PacketRegistry registry, Channel channel) {
        packetResponseManager.noId(packet);
        return channel.writeAndFlush(new Pair<>(packet, registry));
    }

    public RestAction<PacketHandlerContext<?>> sendWithResponse(Packet packet, PacketRegistry registry, Channel channel, long timeout) {
        return packetResponseManager.submit(packet, ()-> channel.writeAndFlush(new Pair<>(packet, registry)), timeout, TimeUnit.MILLISECONDS);
    }

    public ChannelGroup getChannels(){
        return channels;
    }

}
