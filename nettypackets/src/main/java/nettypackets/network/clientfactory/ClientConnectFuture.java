package nettypackets.network.clientfactory;

import io.netty.bootstrap.Bootstrap;
import nettypackets.network.AbstractNetworkConnectFuture;
import nettypackets.network.client.Client;

public class ClientConnectFuture extends AbstractNetworkConnectFuture<Client, Bootstrap> {

    public ClientConnectFuture(Client network, Bootstrap boostrap) {
        super(network, boostrap);
    }
}
