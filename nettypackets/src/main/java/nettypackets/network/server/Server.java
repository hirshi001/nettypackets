package nettypackets.network.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import nettypackets.network.listeners.serverlistener.ServerListener;
import nettypackets.network.listeners.serverlistener.ServerListenerHandler;
import nettypackets.network.packethandlercontext.PacketHandlerContext;
import nettypackets.networkdata.NetworkData;
import nettypackets.packet.Packet;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.restapi.RestAction;

import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.util.Set;

public class Server implements IServer<Server> {

    protected final TCPServer tcpServer;
    protected final UDPServer udpServer;
    protected final EventExecutor executor;
    protected final NetworkData networkData;
    protected final int port;
    protected final ServerListenerHandler<Server> listenerHandler;

    public Server(int port, NetworkData networkData, @Nullable EventExecutor executor){
        this.port = port;
        this.networkData = networkData;
        this.executor = executor==null?new DefaultEventExecutor():executor;

        tcpServer = new TCPServer(port, networkData, executor, this);
        udpServer = new UDPServer(port, networkData, executor, this);

        listenerHandler = new ServerListenerHandler<>();

    }

    public ChannelFuture startTCPServer(ServerBootstrap bootstrap){
        return tcpServer.connect(bootstrap);
    }

    public ChannelFuture startUDPServer(Bootstrap bootstrap){
        return udpServer.connect(bootstrap);
    }

    public ChannelGroup getTCPChannels(){
        return tcpServer.getChannels();
    }

    public Set<InetSocketAddress> getUDPConnections(){
        return udpServer.getConnections();
    }

    public ChannelFuture sendTCP(Packet packet, PacketRegistry registry, Channel channel) {
        return tcpServer.send(packet, registry, channel);
    }

    public ChannelGroupFuture sendTCPToAll(Packet packet, PacketRegistry registry) {
        return tcpServer.sendToAll(packet, registry);
    }

    public RestAction<PacketHandlerContext<?>> sendTCPWithResponse(Packet packet, PacketRegistry registry, Channel channel, long timeout) {
        return tcpServer.sendWithResponse(packet, registry, channel, timeout);
    }

    public ChannelFuture sendUDP(Packet packet, PacketRegistry registry, InetSocketAddress address) {
        return udpServer.send(packet, registry, address);
    }

    public void sendUDPToAll(Packet packet, PacketRegistry registry) {
         udpServer.sendToAll(packet, registry);
    }

    public RestAction<PacketHandlerContext<?>> sendUDPWithResponse(Packet packet, PacketRegistry registry, InetSocketAddress address, long timeout) {
        return udpServer.sendWithResponse(packet, registry, address, timeout);
    }


    @Override
    public NetworkData getNetworkData() {
        return networkData;
    }

    @Override
    public boolean isClient() {
        return false;
    }

    @Override
    public boolean isServer() {
        return true;
    }

    /*
        Need to test this
     */
    @Override
    public Future<?> disconnect() {
        Promise<Server> promise = executor.newPromise();
        if(!tcpServer.isConnected()){
            udpServer.disconnect().addListener(f -> {
                if(f.isSuccess()){
                    promise.setSuccess(Server.this);
                }else{
                    promise.setFailure(f.cause());
                }
            });
            return promise;
        }

        if(!udpServer.isConnected()){
            tcpServer.disconnect().addListener(f -> {
                if(f.isSuccess()){
                    promise.setSuccess(Server.this);
                }else{
                    promise.setFailure(f.cause());
                }
            });
            return promise;
        }


        final boolean[] success = new boolean[2];
        tcpServer.disconnect().addListener(f -> {
            if(f.isSuccess()){
                synchronized (success) {
                    success[0] = true;
                    if(success[1]) promise.setSuccess(Server.this);
                }
            }else{
                promise.setFailure(f.cause());
            }
        });
        udpServer.disconnect().addListener(f -> {
            if(f.isSuccess()){
                synchronized (success) {
                    success[1] = true;
                    if(success[0]) promise.setSuccess(Server.this);
                }
            }else {
                promise.setFailure(f.cause());
            }
        });
        return promise;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void addListener(ServerListener<Server> listener) {
        listenerHandler.addListener(listener);
    }

    @Override
    public void removeListener(ServerListener<Server> listener) {
        listenerHandler.removeListener(listener);
    }

    @SafeVarargs
    @Override
    public final void addListeners(ServerListener<Server>... listeners) {
        for(ServerListener<Server> listener : listeners){
            listenerHandler.addListener(listener);
        }
    }

    @SafeVarargs
    @Override
    public final void removeListeners(ServerListener<Server>... listeners) {
        for(ServerListener<Server> listener : listeners){
            listenerHandler.removeListener(listener);
        }
    }
}
