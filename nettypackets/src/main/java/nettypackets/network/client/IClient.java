package nettypackets.network.client;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.util.concurrent.Future;
import nettypackets.network.NetworkSide;
import nettypackets.network.listeners.clientlistener.ClientListener;
import nettypackets.network.packethandlercontext.PacketHandlerContext;
import nettypackets.packet.Packet;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.restapi.RestAction;

public interface IClient<C extends IClient<C>> extends NetworkSide {

    @Override
    default boolean isClient(){
        return true;
    }

    @Override
    default boolean isServer(){
        return false;
    }

    public ChannelFuture connect(Bootstrap bootstrap);

    public Future<?> disconnect();

    public boolean isConnected();

    public boolean addListener(ClientListener<C> listener);

    public boolean removeListener(ClientListener<C> listener);

    public void addListeners(ClientListener<C>... listeners);

    public void removeListeners(ClientListener<C>... listeners);


}
