package nettypackets.network.client;

import io.netty.channel.Channel;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import nettypackets.network.PacketResponseManager;
import nettypackets.network.listeners.clientlistener.ClientListener;
import nettypackets.network.listeners.clientlistener.ClientListenerHandler;
import nettypackets.network.packethandlercontext.PacketType;
import nettypackets.networkdata.NetworkData;

import javax.annotation.Nullable;

public abstract class AbstractClient<T extends IClient<T>> implements IClient<T> {

    protected final int port;
    protected final String host;
    protected final NetworkData networkData;

    public Channel channel;
    protected final ClientListenerHandler listenerHandler;

    protected final PacketResponseManager packetResponseManager;
    protected final EventExecutor eventExecutor;
    protected PacketType type;

    protected IClient parentSide;

    public AbstractClient(String host, int port, NetworkData networkData, @Nullable EventExecutor eventExecutor, PacketType type, IClient parentSide) {
        this.host = host;
        this.port = port;
        this.networkData = networkData;
        this.eventExecutor = eventExecutor==null?new DefaultEventExecutor():eventExecutor;
        this.listenerHandler = new ClientListenerHandler();
        this.packetResponseManager = new PacketResponseManager(eventExecutor);
        this.type = type;
        this.parentSide = parentSide;
    }

    @Override
    public NetworkData getNetworkData() {
        return networkData;
    }

    @Override
    public boolean addListener(ClientListener<T> listener) {
        return listenerHandler.addListener(listener);
    }

    @Override
    public boolean removeListener(ClientListener<T> listener) {
        return listenerHandler.removeListener(listener);
    }

    @Override
    public void addListeners(ClientListener<T>... listeners) {
        for(ClientListener<T> listener:listeners){
            addListener(listener);
        }
    }

    @Override
    public void removeListeners(ClientListener<T>... listeners) {
        for(ClientListener<T> listener:listeners){
            removeListener(listener);
        }
    }

    protected boolean tcp(){
        return type==PacketType.TCP;
    }

    protected boolean udp(){
        return type==PacketType.UDP;
    }

    protected IClient parentOrThis(){
        return parentSide==null?this:parentSide;
    }
}
