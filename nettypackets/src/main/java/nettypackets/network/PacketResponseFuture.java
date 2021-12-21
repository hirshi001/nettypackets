package nettypackets.network;

import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import nettypackets.packet.Packet;

public class PacketResponseFuture extends DefaultPromise<Packet> {

    private final int packetResponseId;

    public PacketResponseFuture(int packetResponseId, EventExecutor executor) {
        super(executor);
        this.packetResponseId = packetResponseId;
    }

    public int getPacketResponseId() {
        return packetResponseId;

    }

}
