package example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import nettypackets.network.client.Client;
import nettypackets.network.clientfactory.ClientFactory;
import nettypackets.packet.PacketHolder;
import nettypackets.packetdecoderencoder.PacketEncoderDecoder;
import nettypackets.packetdecoderencoder.SimplePacketEncoderDecoder;
import nettypackets.packetregistry.PacketRegistry;

public class MainClient {

    public static PacketEncoderDecoder encoderDecoder;
    public static Client client;

    public static PacketRegistry defaultRegistry;

    public static void main(String[] args) {
        encoderDecoder = new SimplePacketEncoderDecoder();
        ClientFactory clientFactory = ClientFactory.multiPacketRegistryClientFactory().
                setPacketEncoderDecoder(encoderDecoder).
                setPort(8080).
                setIpAddress("localhost").
                setBootstrap(new Bootstrap().
                        group(new NioEventLoopGroup()).
                        channel(NioSocketChannel.class).
                        option(ChannelOption.SO_KEEPALIVE, true));

        //here, you can either use the default packet registry provided or create your own
        //make sure you do the same on the server as well
        defaultRegistry = clientFactory.getPacketRegistryContainer().getDefaultRegistry();
        defaultRegistry.register(new PacketHolder<>(PersonPacket::new, packet -> {
            System.out.println("Person Packet received on Client: " + packet.name + ", " + packet.age);
        }, PersonPacket.class), 0);
        defaultRegistry.register(new PacketHolder<>(})


    }

}
