package nettypackets.network.listeners.clientlistener;

import nettypackets.network.listeners.PacketListener;
import nettypackets.network.client.IClient;

public interface ClientListener<C extends IClient> extends PacketListener<C> {

    void connectedUDP(C client);

    void disconnectedUDP(C client);

    void connectedTCP(C client);

    void disconnectedTCP(C client);

}
