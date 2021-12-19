package nettypackets.network.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import nettypackets.network.NetworkSide;
import nettypackets.network.PacketResponseFuture;
import nettypackets.packet.Packet;

public interface Client extends NetworkSide<Bootstrap> {

    public ChannelFuture sendPacket(Packet packet);

    public PacketResponseFuture sendPacketWithResponse(Packet packet, long timeout);

    public ChannelFuture connect(Bootstrap bootstrap);

    public Future<?> disconnect();

    public boolean isConnected();
}
