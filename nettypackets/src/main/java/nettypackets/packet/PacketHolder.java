package nettypackets.packet;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.function.Supplier;

public class PacketHolder<T extends Packet>{

    public final PacketHandler<T> handler;
    public final Class<T> packetClass;
    public final Supplier<T> supplier;

    public PacketHolder(Supplier<T> supplier, PacketHandler<T> handler, Class<T> packetClass){
        this.supplier = supplier;
        this.handler = handler;
        this.packetClass = packetClass;
    }

    public Packet handlePacket(ByteBuf buf, ChannelHandlerContext ctx, int size){
        T packet = supplier.get();
        int numBytes = buf.readableBytes();
        try {
            packet.readBytes(buf);
            if(handler!=null)handler.handle(packet, ctx);
        }catch(Exception e){
            e.printStackTrace();
            System.err.println(this + " | Number of Bytes = "+numBytes + ", Size = "+size);
        } finally {
            buf.release();
        }
        return packet;
    }

}
