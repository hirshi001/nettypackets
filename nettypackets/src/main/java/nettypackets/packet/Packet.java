package nettypackets.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import nettypackets.PacketHandlerContext;
import nettypackets.packetregistry.PacketRegistry;

public abstract class Packet {

    public PacketHandlerContext packetHandlerContext = new PacketHandlerContext();

    public Packet(){}

    public void writeBytes(ByteBuf out){ }

    public void readBytes(ByteBuf in){  }

    public final void handle(ChannelHandlerContext ctx){
        packetHandlerContext.packetHandler.handle(this, ctx);
    }

}
