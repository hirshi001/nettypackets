package nettypackets.network.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.DatagramChannel;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import nettypackets.iohandlers.*;
import nettypackets.network.packethandlercontext.PacketHandlerContext;
import nettypackets.network.packethandlercontext.PacketType;
import nettypackets.networkdata.NetworkData;
import nettypackets.packet.Packet;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.restapi.RestAction;
import nettypackets.util.tuple.Pair;
import nettypackets.util.defaultpackets.udppackets.UDPInitialConnectionPacket;
import nettypackets.util.tuple.Triple;

import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class UDPClient extends AbstractClient<UDPClient>{

    protected EventLoopGroup workerGroup;
    protected volatile boolean connected;


    public UDPClient(String host, int port, NetworkData networkData, @Nullable EventExecutor eventExecutor) {
        this(host, port, networkData, eventExecutor, null);
    }



    public UDPClient(String host, int port, NetworkData networkData, @Nullable EventExecutor eventExecutor, IClient parentSide) {
        super(host, port, networkData, eventExecutor, PacketType.UDP, parentSide);
        getNetworkData().getPacketRegistryContainer().getDefaultRegistry().registerUDPHelperPackets();
    }

    @Override
    public ChannelFuture connect(Bootstrap bootstrap) {
        bootstrap.handler(new ChannelInitializer<DatagramChannel>() {
            @Override
            public void initChannel(DatagramChannel ch) throws Exception {
                DatagramPacketOutboundEncoder packetOutboundEncoder = new DatagramPacketOutboundEncoder(UDPClient.this){
                    @Override
                    protected void encode(ChannelHandlerContext ctx, Triple<Packet, PacketRegistry, InetSocketAddress> msg, List<Object> out) throws Exception {
                        super.encode(ctx, msg, out);
                        listenerHandler.udpPacketWritten(msg.a, msg.b, ctx, UDPClient.this);
                    }
                };

                DatagramPacketInboundDecoder packetInboundDecoder = new DatagramPacketInboundDecoder(UDPClient.this);
                PacketHandler packetHandler = new PacketHandler(){
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, PacketHandlerContext<?> msg) throws Exception {
                        super.channelRead0(ctx, msg);
                        packetResponseManager.success(msg);
                        listenerHandler.udpPacketReceived(msg, UDPClient.this);
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
                sendUDPConnectionPacket();
                listenerHandler.connectedUDP(this);

            }
        });
    }

    private AtomicBoolean sentUDPConnectionPacket = new AtomicBoolean(false);

    private void sendUDPConnectionPacket(){
        sendWithResponse(new UDPInitialConnectionPacket(), getNetworkData().getPacketRegistryContainer().getDefaultRegistry(), 500).getRestFuture().addListener(future -> {
            if(future.isSuccess()){
                sentUDPConnectionPacket.set(true);
                System.out.println("Successfully sent udp connection packet");
            }else if(!sentUDPConnectionPacket.get()){
                sendUDPConnectionPacket();
            }
        }).perform();
    }


    public ChannelFuture send(Packet packet, PacketRegistry registry) {
        return channel.writeAndFlush(new Triple<>(packet, registry, (InetSocketAddress) channel.remoteAddress()));
    }

    public RestAction<PacketHandlerContext<?>> sendWithResponse(Packet packet, PacketRegistry registry, long timeout) {
        return packetResponseManager.submit(packet, ()-> channel.writeAndFlush(new Triple<>(packet, registry, (InetSocketAddress) channel.remoteAddress())), timeout, TimeUnit.MILLISECONDS);
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
