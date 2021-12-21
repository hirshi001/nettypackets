package nettypackets.network.serverfactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.DefaultEventExecutor;
import nettypackets.iohandlers.PacketOutboundEncoder;
import nettypackets.network.server.DefaultServer;
import nettypackets.network.server.Server;
import nettypackets.networkdata.DefaultNetworkData;
import nettypackets.networkdata.NetworkData;
import nettypackets.packetdecoderencoder.PacketEncoderDecoder;
import nettypackets.packetdecoderencoder.ServerPacketDecoder;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.packetregistrycontainer.MultiPacketRegistryContainer;
import nettypackets.packetregistrycontainer.PacketRegistryContainer;
import nettypackets.packetregistrycontainer.SinglePacketRegistryContainer;

public class ServerFactory {

    private int port = 8080;
    private PacketEncoderDecoder packetEncoderDecoder;
    private ServerBootstrap bootstrap;
    private final PacketRegistryContainer packetRegistryContainer;
    private final boolean isMultiPacketRegistry;


    public static ServerFactory multiPacketRegistryServerFactory(){
        return new ServerFactory(new MultiPacketRegistryContainer(), true);
    }

    public static ServerFactory singlePacketRegistryServerFactory(){
        return new ServerFactory(new SinglePacketRegistryContainer(), false);
    }

    private ServerFactory(PacketRegistryContainer packetRegistryContainer, boolean isMultiPacketRegistry){
        this.packetRegistryContainer = packetRegistryContainer;
        this.isMultiPacketRegistry = isMultiPacketRegistry;
    }

    public int getPort() {
        return port;
    }

    public ServerFactory setPort(int port) {
        this.port = port;
        return this;
    }

    public PacketEncoderDecoder getPacketEncoderDecoder() {
        return packetEncoderDecoder;
    }

    public ServerFactory setPacketEncoderDecoder(PacketEncoderDecoder packetEncoderDecoder) {
        this.packetEncoderDecoder = packetEncoderDecoder;
        return this;
    }

    public ServerBootstrap getBootstrap() {
        return bootstrap;
    }

    public ServerFactory setBootstrap(ServerBootstrap bootstrap) {
        this.bootstrap = bootstrap;
        return this;
    }

    public PacketRegistryContainer getPacketRegistryContainer() {
        return packetRegistryContainer;
    }

    public boolean isMultiPacketRegistry() {
        return isMultiPacketRegistry;
    }

    public ServerFactory addPacketRegistry(PacketRegistry packetRegistry){
        packetRegistryContainer.addRegistry(packetRegistry);
        return this;
    }


    public Server connectServerNow() throws InterruptedException {
        Server server = getServer();
        server.connect(bootstrap).sync();
        return server;
    }

    public Server connectServerNowUninterruptibly(){
        Server server = getServer();
        server.connect(bootstrap).syncUninterruptibly();
        return server;
    }

    public ServerConnectFuture connectAsync(){
        Server server = getServer();
        server.connect(bootstrap).syncUninterruptibly();
        return new ServerConnectFuture(server, bootstrap);
    }

    private Server getServer(){
        return new ServerBuilder().build();
    }


    private class ServerBuilder{

        ServerBootstrap bootstrap;
        NetworkData networkData;
        ChannelGroup group;


        public ServerBuilder(){

            networkData = getNetworkData();

            group = new DefaultChannelGroup("connected-channels", new DefaultEventExecutor());
            if(ServerFactory.this.bootstrap != null){
                bootstrap = ServerFactory.this.bootstrap;
            }else{
                bootstrap = new ServerBootstrap();
            }
        }

        public Server build(){
            return new DefaultServer(port, networkData, group);
        }


        private NetworkData getNetworkData(){
            return new DefaultNetworkData(packetEncoderDecoder, packetRegistryContainer);
        }

    }
}
