package nettypackets.network.serverfactory;

import io.netty.bootstrap.ServerBootstrap;
import nettypackets.network.AbstractNetworkConnectFuture;
import nettypackets.network.server.Server;

public class ServerConnectFuture extends AbstractNetworkConnectFuture<Server, ServerBootstrap> {

    public ServerConnectFuture(Server network, ServerBootstrap boostrap) {
        super(network, boostrap);
    }

}
