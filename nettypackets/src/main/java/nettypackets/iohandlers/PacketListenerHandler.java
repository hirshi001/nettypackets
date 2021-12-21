package nettypackets.iohandlers;

import io.netty.channel.ChannelHandlerContext;
import nettypackets.network.NetworkSide;
import nettypackets.packet.Packet;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class PacketListenerHandler<S extends NetworkSide<?>, T extends PacketListener<S>> implements PacketListener<S>{

    protected Collection<T> packetListeners;

    public PacketListenerHandler(){
        packetListeners = ConcurrentHashMap.newKeySet();
    }

    public PacketListenerHandler(Collection<T> dataStructure) {
        packetListeners = dataStructure;
    }

    @Override
    public void packetReceived(Packet packet, ChannelHandlerContext context, S side) {
        packetListeners.forEach(listener -> listener.packetReceived(packet, context, side));

    }

    @Override
    public void packetWritten(Packet packet, ChannelHandlerContext context, S side) {
        packetListeners.forEach(listener -> listener.packetWritten(packet, context, side));
    }

    public boolean addListener(T listener){
        return packetListeners.add(listener);
    }

    public boolean removeListener(T listener){
        return packetListeners.remove(listener);
    }

    public Collection<T> getPacketListeners(){
        return packetListeners;
    }

}
