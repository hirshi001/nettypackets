package nettypackets.network.listeners.serverlistener;

import io.netty.channel.ChannelHandlerContext;
import nettypackets.network.listeners.PacketListener;
import nettypackets.network.server.IServer;

public interface ServerListener<S extends IServer<?>> extends PacketListener<S> {

    public void tcpClientDisconnected(S server, ChannelHandlerContext context);

    public void tcpClientConnected(S server, ChannelHandlerContext context);

    public void udpClientDisconnected(S server, ChannelHandlerContext context);

    public void udpClientConnected(S server, ChannelHandlerContext context);

    public void udpConnected(S server);

    public void tcpConnected(S server);

    public void udpDisconnected(S server);

    public void tcpDisconnected(S server);

}
