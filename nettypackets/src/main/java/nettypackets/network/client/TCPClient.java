package nettypackets.network.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import nettypackets.iohandlers.PacketHandler;
import nettypackets.iohandlers.PacketInboundDecoder;
import nettypackets.iohandlers.PacketOutboundEncoder;
import nettypackets.network.packethandlercontext.PacketHandlerContext;
import nettypackets.network.packethandlercontext.PacketType;
import nettypackets.networkdata.NetworkData;
import nettypackets.packet.Packet;
import nettypackets.packet.PacketHolder;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.restapi.RestAction;
import nettypackets.util.defaultpackets.systempackets.SetPacketRegistryIDPacket;
import nettypackets.util.tuple.Pair;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

public class TCPClient extends AbstractClient<TCPClient>{


    protected EventLoopGroup workerGroup;
    protected volatile boolean connected;
    public TCPClient(String host, int port, NetworkData networkData, @Nullable EventExecutor eventExecutor) {
       this(host, port, networkData, eventExecutor, null);
    }

    public TCPClient(String host, int port, NetworkData networkData, @Nullable EventExecutor eventExecutor, IClient parentSide){
        super(host, port, networkData, eventExecutor, PacketType.TCP, parentSide);
    }


    public ChannelFuture connect(Bootstrap bootstrap) {
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            public void initChannel(Channel ch) throws Exception {
                PacketOutboundEncoder packetOutboundEncoder = new PacketOutboundEncoder(parentOrThis()){
                    @Override
                    protected void encode(ChannelHandlerContext ctx, Pair<Packet, PacketRegistry> msg, ByteBuf out) throws Exception {
                        super.encode(ctx, msg, out);
                        listenerHandler.tcpPacketWritten(msg.a, msg.b, ctx, parentOrThis());
                    }
                };

                PacketInboundDecoder packetInboundDecoder = new PacketInboundDecoder(parentOrThis());
                PacketHandler packetHandler = new PacketHandler(){
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, PacketHandlerContext<?> msg) throws Exception {
                        super.channelRead0(ctx, msg);
                        System.out.println("RECEIVED");
                        packetResponseManager.success(msg);
                        listenerHandler.tcpPacketReceived(msg, TCPClient.this);
                    }

                    @Override
                    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                        super.channelRegistered(ctx);
                        channel = ctx.channel();
                        listenerHandler.disconnectedTCP(TCPClient.this);
                    }

                    @Override
                    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
                        super.channelUnregistered(ctx);
                        channel = null;
                        listenerHandler.disconnectedTCP(TCPClient.this);
                    }
                };
                ch.pipeline().addLast(packetOutboundEncoder, packetInboundDecoder, packetHandler);
            }
        });

        workerGroup = bootstrap.config().group();
        return bootstrap.connect(host, port).addListener((ChannelFutureListener) future -> {
            if(future.isSuccess()){
                connected = true;
                channel = future.channel();
                listenerHandler.connectedTCP(this);
            }
        });
    }


    public ChannelFuture send(Packet packet, PacketRegistry registry) {
        packetResponseManager.noId(packet);
        return channel.writeAndFlush(new Pair<>(packet, registry));
    }

    public RestAction<PacketHandlerContext<?>> sendWithResponse(Packet packet, PacketRegistry registry, long timeout) {
        return packetResponseManager.submit(packet, ()-> channel.writeAndFlush(new Pair<>(packet, registry)), timeout, TimeUnit.MILLISECONDS);
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
