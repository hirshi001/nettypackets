package nettypackets;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import nettypackets.encoderdecoder.PacketDecoder;
import nettypackets.encoderdecoder.PacketEncoder;
import nettypackets.packet.Packet;
import nettypackets.packetregistry.DefaultPacketRegistry;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.packetregistry.SidedPacketRegistryContainer;

import java.util.List;

public class Client {

    private final int port;
    private final String host;
    private final SidedPacketRegistryContainer clientRegistries;

    public ChannelHandlerContext channel;

    public Client(String host, int port, SidedPacketRegistryContainer clientRegistries) {
        this.port = port;
        this.host = host;
        this.clientRegistries = clientRegistries;
    }

    public void sendPacket(PacketRegistry registry, Packet p) {
        channel.channel().write(new Pair<>(registry, p));
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
                            new PacketDecoder(clientRegistries){
                                @Override
                                protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
                                    System.out.println("bytes recieved: " + in.toString());
                                    super.decode(ctx, in, out);
                                }

                                @Override
                                public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                                    super.channelRegistered(ctx);
                                    channel = ctx;
                                }
                            },
                            new PacketEncoder()
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
