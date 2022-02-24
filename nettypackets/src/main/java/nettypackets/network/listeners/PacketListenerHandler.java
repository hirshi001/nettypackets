package nettypackets.network.listeners;

import io.netty.channel.ChannelHandlerContext;
import nettypackets.network.NetworkSide;
import nettypackets.network.packethandlercontext.PacketHandlerContext;
import nettypackets.packet.Packet;
import nettypackets.packetregistry.PacketRegistry;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class PacketListenerHandler<S extends NetworkSide, T extends PacketListener<S>> implements PacketListener<S>{

    protected Collection<T> packetListeners;

    public PacketListenerHandler(){
        packetListeners = ConcurrentHashMap.newKeySet();
    }

    public PacketListenerHandler(Collection<T> dataStructure) {
        packetListeners = dataStructure;
    }


    @Override
    public void tcpPacketWritten(Packet packet, PacketRegistry registry, ChannelHandlerContext context, S side) {
        perform(listener -> listener.tcpPacketWritten(packet, registry, context, side));
    }

    @Override
    public void tcpPacketReceived(PacketHandlerContext<?> context, S side) {
        perform(listener -> listener.tcpPacketReceived(context, side));
    }

    @Override
    public void udpPacketReceived(PacketHandlerContext<?> context, S side) {
        perform(listener -> listener.udpPacketReceived(context, side));
    }

    @Override
    public void udpPacketWritten(Packet packet, PacketRegistry registry, ChannelHandlerContext context, S side) {
        perform(listener -> listener.udpPacketWritten(packet, registry, context, side));
    }


    public boolean addListener(T listener){
        return packetListeners.add(listener);
    }

    public boolean removeListener(T listener){
        return packetListeners.remove(listener);
    }

    public void addAllListeners(Collection<T> listeners){
        packetListeners.addAll(listeners);
    }

    public void removeAllListeners(Collection<T> listeners){
        packetListeners.removeAll(listeners);
    }

    public Collection<T> getPacketListeners(){
        return packetListeners;
    }


    protected void perform(ListenerRunnable<T> serverListenerRunnable){
        getPacketListeners().forEach(serverListenerRunnable::run);
    }

    @FunctionalInterface
    protected interface ListenerRunnable<T>{
        void run(T serverListener);
    }

}
