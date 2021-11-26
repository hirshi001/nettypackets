package nettypackets.packet;


import io.netty.buffer.ByteBuf;
import nettypackets.PacketHandlerContext;

import java.util.function.Supplier;

public class PacketHolder<T extends Packet>{

    public final PacketHandler<T> handler;
    public final Class<T> packetClass;
    public final Supplier<T> supplier;

    public PacketHolder(Supplier<T> supplier, PacketHandler<T> handler, Class<T> packetClass){
        this.supplier = supplier;
        if(handler==null) this.handler = PacketHandler.noHandle();
        else this.handler = handler;
        this.packetClass = packetClass;
    }

    public Packet getPacket(ByteBuf buf){
        T packet = supplier.get();
        packet.readBytes(buf);
        return packet;
    }

}
