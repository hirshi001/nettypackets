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
import io.netty.util.concurrent.GlobalEventExecutor;
import nettypackets.packet.Packet;
import nettypackets.packet.PacketHelper;

public class Server {

    private final int port;
    private final SidedPacketRegistryContainer serverRegistries;
    public final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    public Server(int port, SidedPacketRegistryContainer serverRegistries) {
        this.port = port;
        this.serverRegistries = serverRegistries;
    }

    public void sendPacketToAllConnected(PacketRegistry registry, Packet msg) {
        ByteBuf buf = PacketHelper.toBytes(registry, msg);
        channels.writeAndFlush(buf);
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
                            ch.pipeline().addLast(new ServerPacketDecoder(serverRegistries, channels));
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


}
