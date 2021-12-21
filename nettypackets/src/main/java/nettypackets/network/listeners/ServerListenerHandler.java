package nettypackets.network.listeners;

import io.netty.channel.ChannelHandlerContext;
import nettypackets.iohandlers.PacketListenerHandler;
import nettypackets.network.server.Server;

public class ServerListenerHandler extends PacketListenerHandler<Server, ServerListener> implements ServerListener{
    @Override
    public void clientDisconnected(Server server, ChannelHandlerContext context) {
        getPacketListeners().forEach(listener -> listener.clientDisconnected(server, context));
    }

    @Override
    public void disconnected(Server server) {
        getPacketListeners().forEach(listener -> listener.disconnected(server));
    }

    @Override
    public void clientConnected(Server server, ChannelHandlerContext context) {
        getPacketListeners().forEach(listener -> listener.clientConnected(server, context));
    }
}
