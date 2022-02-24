package nettypackets.network.listeners.serverlistener;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import nettypackets.network.listeners.PacketListenerHandler;
import nettypackets.network.server.IServer;

import java.util.Collection;

public class ServerListenerHandler<S extends IServer<S>> extends PacketListenerHandler<S, ServerListener<S>> implements ServerListener<S>{

    public ServerListenerHandler() {
        super();
    }

    public ServerListenerHandler(Collection<ServerListener<S>> dataStructure) {
        super(dataStructure);
    }

    @Override
    public void udpDisconnected(S server) {
        perform(listener -> listener.udpDisconnected(server));
    }

    @Override
    public void tcpDisconnected(S server) {
        perform(listener -> listener.tcpDisconnected(server));
    }

    @Override
    public void tcpClientDisconnected(S server, ChannelHandlerContext context) {
        perform(listener -> listener.tcpClientDisconnected(server, context));
    }

    @Override
    public void tcpClientConnected(S server, ChannelHandlerContext context) {
        perform(listener -> listener.tcpClientConnected(server, context));
    }

    @Override
    public void udpClientDisconnected(S server, ChannelHandlerContext context) {
        perform(listener -> listener.udpClientDisconnected(server, context));
    }

    @Override
    public void udpClientConnected(S server, ChannelHandlerContext context) {
        perform(listener -> listener.udpClientConnected(server, context));
    }

    @Override
    public void udpConnected(S server) {
        perform(listener -> listener.udpConnected(server));
    }

    @Override
    public void tcpConnected(S server) {
        perform(listener -> listener.tcpConnected(server));
    }

}
