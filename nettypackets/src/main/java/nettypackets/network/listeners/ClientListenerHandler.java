package nettypackets.network.listeners;

import nettypackets.iohandlers.PacketListenerHandler;
import nettypackets.network.client.Client;

import java.util.Collection;

public class ClientListenerHandler extends PacketListenerHandler<Client, ClientListener> implements ClientListener{


    public ClientListenerHandler(){
    }

    public ClientListenerHandler(Collection<ClientListener> dataStructure) {
        super(dataStructure);
    }

    @Override
    public void disconnected(Client client) {
        getPacketListeners().forEach(listener -> listener.disconnected(client));
    }

}
