package nettypackets;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import nettypackets.iohandlers.PacketOutboundDecoder;
import nettypackets.iohandlers.PacketInboundEncoder;
import nettypackets.networkdata.NetworkData;
import nettypackets.packet.Packet;

public class Client {

    private final int port;
    private final String host;
    private final NetworkData networkData;
    public int packetsSent = 0, packetsReceived = 0;

    public ChannelHandlerContext channel;

    public Client(String host, int port, NetworkData networkData) {
        this.port = port;
        this.host = host;
        this.networkData = networkData;
    }

    public void sendPacket(Packet packet) {
        channel.channel().writeAndFlush(packet);
    }

    public void run() throws Exception {

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(
                            new PacketOutboundDecoder(networkData){
                                @Override
                                public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                                    super.channelRegistered(ctx);
                                    channel = ctx;
                                }
                            },
                            new PacketInboundEncoder(networkData){
                                @Override
                                protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
                                    super.encode(ctx, msg, out);
                                    packetsSent++;
                                }
                            }
                    );
                }
            });

            ChannelFuture f = b.connect(host, port).sync();

            try {
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }

}
