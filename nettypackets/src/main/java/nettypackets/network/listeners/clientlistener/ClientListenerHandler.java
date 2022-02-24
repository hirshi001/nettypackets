package nettypackets.network.listeners.clientlistener;

import nettypackets.network.client.IClient;
import nettypackets.network.listeners.PacketListenerHandler;

import java.util.Collection;

public class ClientListenerHandler<C extends IClient> extends PacketListenerHandler<C, ClientListener<C>> implements ClientListener<C>{


    public ClientListenerHandler(){
    }

    public ClientListenerHandler(Collection<ClientListener<C>> dataStructure) {
        super(dataStructure);
    }

    @Override
    public void connectedUDP(C client) {
        perform(clientListener -> clientListener.connectedUDP(client));
    }

    @Override
    public void disconnectedUDP(C client) {
        perform(clientListener -> clientListener.disconnectedUDP(client));
    }

    @Override
    public void connectedTCP(C client) {
        perform(clientListener -> clientListener.connectedTCP(client));
    }

    @Override
    public void disconnectedTCP(C client) {
        perform(clientListener -> clientListener.disconnectedTCP(client));
    }
}
