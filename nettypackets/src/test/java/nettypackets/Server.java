package nettypackets;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutor;
import nettypackets.iohandlers.PacketInboundEncoder;
import nettypackets.networkdata.NetworkData;
import nettypackets.packet.Packet;
import nettypackets.packetdecoderencoder.PacketEncoderDecoder;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.packetregistry.SidedPacketRegistryContainer;

import java.util.List;

public class Server {

    private final int port;
    private final NetworkData networkData;
    public final ChannelGroup channels = new DefaultChannelGroup("connected-channels", new DefaultEventExecutor());
    public int packetsSent = 0, packetsReceived = 0;

    public Server(int port, NetworkData networkData) {
        this.port = port;
        this.networkData = networkData;
    }

    public void sendPacketToAllConnected(Packet msg) {
        channels.writeAndFlush(msg);
    }

    public void startUp() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new ServerPacketDecoder(networkData, channels){
                                        @Override
                                        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
                                            Packet packet;
                                            while ((packet = super.getPacket(in, ctx)) != null) {
                                                packet.handle(ctx);
                                                packetsReceived++;
                                            }
                                        }
                                    },
                                    new PacketInboundEncoder(networkData){
                                        @Override
                                        protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
                                            packetsSent++;
                                            super.encode(ctx, msg, out);
                                        }
                                    });
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)


            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            channels.close().awaitUninterruptibly();
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    /*
        A method which checks if 2 line segments intersect
        The arguments are integers where each pair of integers represent an endpoint of the line
     */
    public static boolean intersect(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        int d = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        if (d == 0) return false;

        int n = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
        int m = (x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3);

        int u = n / d;
        int v = m / d;

        return u >= 0 && u <= 1 && v >= 0 && v <= 1;
    }
}


