package nettypackets.network.listeners;

import io.netty.channel.ChannelHandlerContext;
import nettypackets.iohandlers.PacketListener;
import nettypackets.network.server.Server;

public interface ServerListener extends PacketListener<Server> {

    public void clientDisconnected(Server server, ChannelHandlerContext context);

    public void clientConnected(Server server, ChannelHandlerContext context);

    public void disconnected(Server server);

}
