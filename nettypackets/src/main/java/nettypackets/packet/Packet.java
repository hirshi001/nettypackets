package nettypackets.packet;

import io.netty.buffer.ByteBuf;
import nettypackets.PacketHandlerContext;
import nettypackets.packetregistry.PacketRegistry;

public abstract class Packet {

    public PacketHandler<Packet> packetHandler;
    public PacketRegistry packetRegistry;

    public Packet(){}

    public void writeBytes(ByteBuf out){ }

    public void readBytes(ByteBuf in){  }

    public final void handle(PacketHandlerContext ctx){
        packetHandler.handle(this, ctx);
    }

}
