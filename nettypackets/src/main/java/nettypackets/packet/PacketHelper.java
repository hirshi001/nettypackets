package nettypackets.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import nettypackets.ByteBufUtil;
import nettypackets.PacketRegistry;
import nettypackets.SidedPacketRegistryContainer;

public class PacketHelper {

    public static ByteBuf toBytes(PacketRegistry registry, Packet p){
        return toBytes(Unpooled.buffer(32), registry, p);
    }

    public static ByteBuf toBytes(ByteBuf buf, PacketRegistry registry, Packet p){
        int startIndex = buf.writerIndex();
        buf.writerIndex(startIndex+8);

        ByteBufUtil.writeStringToBuf(registry.registryName, buf);
        p.writeBytes(buf);

        int size = buf.writerIndex()-startIndex-8;
        int lastIdx = buf.writerIndex();

        buf.writerIndex(startIndex);

        buf.writeInt(size);
        buf.writeInt(registry.getId(p.getClass()));

        buf.writerIndex(lastIdx);
        return buf;
    }

    public static Packet handle(ByteBuf buf, ChannelHandlerContext ctx, SidedPacketRegistryContainer packetRegistries) throws Exception{
        if(buf.readableBytes()<4) return null;

        //get the size
        int size = buf.getInt(buf.readerIndex());

        if(buf.readableBytes()<size+8) return null;
        buf.readInt();

        //get the id
        int id = buf.readInt();

        ByteBuf msg = buf.readBytes(size);

        //get the registry
        String namespace = ByteBufUtil.readStringFromBuf(msg);
        PacketRegistry registry = packetRegistries.get(namespace);

        msg.markWriterIndex();

        //get the packet holder
        PacketHolder<? extends Packet> holder = registry.getPacketHolder(id);
        return holder.handlePacket(msg, ctx, size);
    }

}
