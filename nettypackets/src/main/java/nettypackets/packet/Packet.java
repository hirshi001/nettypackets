package nettypackets.packet;

import io.netty.buffer.ByteBuf;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.util.ByteBufSerializable;

public abstract class Packet implements ByteBufSerializable {

    public int sendingId = -1, receivingId = -1;

    public Packet(){}

    @Override
    public void writeBytes(ByteBuf out){
        out.writeInt(sendingId);
        out.writeInt(receivingId);
    }

    @Override
    public void readBytes(ByteBuf in){
        sendingId = in.readInt();
        receivingId = in.readInt();
    }

    /**
     * Sets the packet which this packet is responding to (if it is responding to any packet at all)
     * @param packet
     */
    public Packet setResponsePacket(Packet packet){
        int sId = packet.receivingId;
        int rId = packet.sendingId;
        this.sendingId = sId;
        this.receivingId = rId;
        return this;
    }

}
