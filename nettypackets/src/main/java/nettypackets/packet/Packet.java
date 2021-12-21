package nettypackets.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import nettypackets.networkdata.NetworkData;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.util.PacketHandlerContext;

public abstract class Packet {

    public PacketHandlerContext packetHandlerContext = new PacketHandlerContext();
    public NetworkData networkData;
    public int clientId = -1, serverId = -1;

    public Packet(){}

    public void writeBytes(ByteBuf out){
        out.writeInt(clientId);
        out.writeInt(serverId);
    }

    public void readBytes(ByteBuf in){
        clientId = in.readInt();
        serverId = in.readInt();
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
        this.clientId = packet.clientId;
        this.serverId = packet.serverId;
        return this;
    }

}
