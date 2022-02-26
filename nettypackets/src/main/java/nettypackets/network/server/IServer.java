package nettypackets.network.server;

import io.netty.util.concurrent.Future;
import nettypackets.network.NetworkSide;
import nettypackets.network.listeners.serverlistener.ServerListener;
import nettypackets.packet.PacketHolder;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.packetregistrycontainer.PacketRegistryContainer;
import nettypackets.util.defaultpackets.systempackets.SetPacketRegistryIDPacket;

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
