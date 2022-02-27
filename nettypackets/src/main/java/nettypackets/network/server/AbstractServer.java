package nettypackets.network.server;

import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import nettypackets.network.PacketResponseManager;
import nettypackets.network.listeners.serverlistener.ServerListener;
import nettypackets.network.listeners.serverlistener.ServerListenerHandler;
import nettypackets.network.packethandlercontext.PacketType;
import nettypackets.networkdata.NetworkData;
import org.jetbrains.annotations.Nullable;


public abstract class AbstractServer<S extends IServer<S>> implements IServer<S> {

    public final int port;
    private final NetworkData networkData;

    protected final PacketResponseManager packetResponseManager;
    protected final ServerListenerHandler listenerHandler;
    protected final EventExecutor eventExecutor;

    protected IServer parentSide;

    public final PacketType packetType;
    public AbstractServer(int port, NetworkData networkData, @Nullable EventExecutor eventExecutor, PacketType packetType, IServer parentSide) {
        super();
        this.port = port;
        this.networkData = networkData;

        this.eventExecutor = eventExecutor==null?new DefaultEventExecutor():eventExecutor;
        this.packetResponseManager = new PacketResponseManager(eventExecutor);
        this.listenerHandler = new ServerListenerHandler<>();
        this.packetType = packetType;
        this.parentSide = parentSide;
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
        return false;
    }


    @Override
    public void addListener(ServerListener<S> listener) {
        listenerHandler.addListener(listener);
    }

    @Override
    public void removeListener(ServerListener<S> listener) {
        listenerHandler.removeListener(listener);
    }

    @Override
    public void addListeners(ServerListener<S>... listeners) {
        for(ServerListener<S> listener:listeners) {
            listenerHandler.addListener(listener);
        }
    }

    @Override
    public void removeListeners(ServerListener<S>... listeners) {
        for(ServerListener<S> listener:listeners) {
            listenerHandler.removeListener(listener);
        }
    }


    protected IServer parentOrThis(){
        return parentSide == null ? this : parentSide;
    }


}
