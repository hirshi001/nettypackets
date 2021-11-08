package nettypackets;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.GlobalEventExecutor;
import nettypackets.encoderdecoder.PacketEncoder;
import nettypackets.packet.Packet;
import nettypackets.packet.PacketHelper;
import nettypackets.packetregistry.DefaultPacketRegistry;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.packetregistry.SidedPacketRegistryContainer;

public class Server {

    private final int port;
    private final SidedPacketRegistryContainer serverRegistries;
    public final ChannelGroup channels = new DefaultChannelGroup("connected-channels", new DefaultEventExecutor());

    public Server(int port, SidedPacketRegistryContainer serverRegistries) {
        this.port = port;
        this.serverRegistries = serverRegistries;
    }

    public void sendPacketToAllConnected(PacketRegistry registry, Packet msg) {
        channels.write(new Pair<>(registry, msg));
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
                            ch.pipeline().addLast(new ServerPacketDecoder(serverRegistries, channels), new PacketEncoder());
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


