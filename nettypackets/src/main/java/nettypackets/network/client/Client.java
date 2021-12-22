package nettypackets.network.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import nettypackets.network.NetworkSide;
import nettypackets.network.listeners.ClientListener;
import nettypackets.packet.Packet;
import nettypackets.restapi.RestAction;

public interface Client extends NetworkSide<Bootstrap> {

    /**
     * sends the packet to the server
     * @param packet
     * @return the ChannelFuture from sending the packet
     */
    public ChannelFuture sendPacket(Packet packet);

    /**
     * sends a packet to the server and returns a future for the response packet send from the server to this client
     * @param packet
     * @param timeout
     * @return the response future with the packet response from the server to this client
     */
    public RestAction<Packet> sendPacketWithResponse(Packet packet, long timeout);

    public ChannelFuture connect(Bootstrap bootstrap);

    /**
     * disconnects the client
     * @return the future of the disconnection
     */
    public Future<?> disconnect();

    /**
     * @return true if the client is connected
     */
    public boolean isConnected();

    /**
     * adds a listener to the client
     * @param listener
     * @return true if the listener was added, false if it was already added
     */
    public boolean addListener(ClientListener listener);

    /**
     * removes a listener from the client
     * @param listener
     * @return true if the listener was removed, false if it was not found
     */
    public boolean removeListener(ClientListener listener);
}
