package nettypackets.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import nettypackets.networkdata.NetworkData;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.util.ByteBufSerializable;
import nettypackets.util.PacketHandlerContext;

public abstract class Packet implements ByteBufSerializable {

    public PacketHandlerContext packetHandlerContext = new PacketHandlerContext();
    public NetworkData networkData;
    public int sendingId = -1, receivingId = -1;

    public Packet(){}

    public void writeBytes(ByteBuf out){
        out.writeInt(sendingId);
        out.writeInt(receivingId);
    }

    public void readBytes(ByteBuf in){
        sendingId = in.readInt();
        receivingId = in.readInt();
    }

    public final void handle(ChannelHandlerContext ctx){
        packetHandlerContext.packetHandler.handle(this);
    }

    public Packet setPacketRegistry(PacketRegistry registry){
        packetHandlerContext.packetRegistry = registry;
        return this;
    }

    public PacketHandlerContext getPacketHandlerContext(){
        return packetHandlerContext;
    }

    public NetworkData getNetworkData(){
        return networkData;
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
