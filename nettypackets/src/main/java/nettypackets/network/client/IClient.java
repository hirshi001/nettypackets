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
import nettypackets.packet.PacketHolder;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.restapi.RestAction;
import nettypackets.util.defaultpackets.systempackets.SetPacketRegistryIDPacket;

public interface IClient<C extends IClient<C>> extends NetworkSide {

    @Override
    default boolean isClient(){
        return true;
    }

    @Override
    default boolean isServer(){
        return false;
    }

    public Future<?> disconnect();

    public boolean isConnected();

    public boolean addListener(ClientListener<C> listener);

    public boolean removeListener(ClientListener<C> listener);

    public void addListeners(ClientListener<C>... listeners);

    public void removeListeners(ClientListener<C>... listeners);

    default public void init(){
        PacketRegistry registry = getNetworkData().getPacketRegistryContainer().getDefaultRegistry();
        registry.registerSystemPackets();
        PacketHolder<SetPacketRegistryIDPacket> holder = (PacketHolder<SetPacketRegistryIDPacket>) registry.getPacketHolder(registry.getId(SetPacketRegistryIDPacket.class));

        if(holder != null) {
            holder.handler = context -> {
                PacketRegistry registryToSet = context.networkData.getPacketRegistryContainer().get(context.packet.registryName);
                context.networkData.getPacketRegistryContainer().setPacketRegistryID(registryToSet, context.packet.registryId);
            };
        }
    }


}
