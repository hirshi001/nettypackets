package nettypackets.network.listeners;

import nettypackets.iohandlers.PacketListener;
import nettypackets.network.client.Client;
import nettypackets.packet.Packet;

public interface ClientListener extends PacketListener<Client> {

    void disconnected(Client client);

    void connected(Client client);

    //void exception(Client client, Exception exception);

}
