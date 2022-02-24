package nettypackets.network.server;

import io.netty.util.concurrent.Future;
import nettypackets.network.NetworkSide;
import nettypackets.network.listeners.serverlistener.ServerListener;

public interface IServer<S extends IServer<S>> extends NetworkSide {

    @Override
    default boolean isClient(){
        return false;
    }

    @Override
    default boolean isServer(){
        return true;
    }

    public Future<?> disconnect();

    public boolean isConnected();

    public void addListener(ServerListener<S> listener);

    public void removeListener(ServerListener<S> listener);

    public void addListeners(ServerListener<S>... listeners);

    public void removeListeners(ServerListener<S>... listeners);

}
