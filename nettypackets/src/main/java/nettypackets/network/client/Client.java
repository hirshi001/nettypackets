package nettypackets.network.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import nettypackets.network.listeners.clientlistener.ClientListener;
import nettypackets.network.listeners.clientlistener.ClientListenerHandler;
import nettypackets.network.packethandlercontext.PacketHandlerContext;
import nettypackets.networkdata.NetworkData;
import nettypackets.packet.Packet;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.restapi.RestAction;

import javax.annotation.Nullable;

public class Client implements IClient<Client> {

    protected final TCPClient tcpClient;
    protected final UDPClient udpClient;
    protected final EventExecutor executor;
    protected final NetworkData networkData;
    protected final String host;
    protected final int port;

    protected final ClientListenerHandler<Client> listenerHandler;

    public Client(String host, int port, NetworkData networkData, @Nullable EventExecutor executor){
        this.host = host;
        this.port = port;
        this.networkData = networkData;
        this.executor = executor==null?new DefaultEventExecutor():executor;

        tcpClient = new TCPClient(host, port, networkData, executor, this);
        udpClient = new UDPClient(host, port, networkData, executor, this);

        listenerHandler = new ClientListenerHandler<>();
    }

    public ChannelFuture startTCPClient(Bootstrap bootstrap){
        return tcpClient.connect(bootstrap);
    }

    public ChannelFuture startUDPClient(Bootstrap bootstrap){
        return udpClient.connect(bootstrap);
    }

    public Channel getTCPChannel(){
        return tcpClient.channel;
    }

    public Channel getUDPChannel(){
        return udpClient.channel;
    }

    public ChannelFuture sendTCP(Packet packet, PacketRegistry registry) {
        return tcpClient.send(packet, registry);
    }

    public RestAction<PacketHandlerContext<?>> sendTCPWithResponse(Packet packet, PacketRegistry registry, long timeout) {
        return tcpClient.sendWithResponse(packet, registry, timeout);
    }

    public ChannelFuture sendUDP(Packet packet, PacketRegistry registry) {
        return udpClient.send(packet, registry);
    }


    public RestAction<PacketHandlerContext<?>> sendUDPWithResponse(Packet packet, PacketRegistry registry, long timeout) {
        return udpClient.sendWithResponse(packet, registry, timeout);
    }


    @Override
    public NetworkData getNetworkData() {
        return networkData;
    }

    @Override
    public ChannelFuture connect(Bootstrap bootstrap) {
        return null;
    }

    /*
            Need to test this
         */
    @Override
    public Future<?> disconnect() {
        Promise<Client> promise = executor.newPromise();

        if(!isConnected()) {
            promise.setSuccess(this);
            return promise;
        }


        if(!udpClient.isConnected()){
            tcpClient.disconnect().addListener(f -> {
                if(f.isSuccess()){
                    promise.setSuccess(Client.this);
                }else{
                    promise.setFailure(f.cause());
                }
            });
            return promise;
        }

        if(!tcpClient.isConnected()){
            udpClient.disconnect().addListener(f -> {
                if(f.isSuccess()){
                    promise.setSuccess(Client.this);
                }else{
                    promise.setFailure(f.cause());
                }
            });
            return promise;
        }


        final boolean[] success = new boolean[2];
        udpClient.disconnect().addListener(f -> {
            if(f.isSuccess()){
                synchronized (success) {
                    success[0] = true;
                    if(success[1]) promise.setSuccess(Client.this);
                }
            }else{
                promise.setFailure(f.cause());
            }
        });
        tcpClient.disconnect().addListener(f -> {
            if(f.isSuccess()){
                synchronized (success) {
                    success[1] = true;
                    if(success[0]) promise.setSuccess(Client.this);
                }
            }else {
                promise.setFailure(f.cause());
            }
        });
        return promise;
    }

    @Override
    public boolean isConnected() {
        return tcpClient.isConnected() || udpClient.isConnected();
    }

    @Override
    public boolean addListener(ClientListener<Client> listener) {
        return listenerHandler.addListener(listener);
    }

    @Override
    public boolean removeListener(ClientListener<Client> listener) {
        return listenerHandler.removeListener(listener);
    }

    @SafeVarargs
    @Override
    public final void addListeners(ClientListener<Client>... listeners) {
        for(ClientListener<Client> listener : listeners){
            listenerHandler.addListener(listener);
        }
    }

    @SafeVarargs
    @Override
    public final void removeListeners(ClientListener<Client>... listeners) {
        for(ClientListener<Client> listener : listeners){
            listenerHandler.removeListener(listener);
        }
    }

}
