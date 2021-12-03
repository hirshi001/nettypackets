package nettypackets.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import nettypackets.util.PacketHandlerContext;
import nettypackets.networkdata.NetworkData;
import nettypackets.packetregistry.PacketRegistry;

public abstract class Packet {

    public PacketHandlerContext packetHandlerContext = new PacketHandlerContext();
    public NetworkData networkData;

    public Packet(){}

    public void writeBytes(ByteBuf out){ }

    public void readBytes(ByteBuf in){  }

    public final void handle(ChannelHandlerContext ctx){
        packetHandlerContext.packetHandler.handle(this, ctx);
    }

    public Packet setPacketRegistry(PacketRegistry registry){
        packetHandlerContext.packetRegistry = registry;
        return this;
    }

}
