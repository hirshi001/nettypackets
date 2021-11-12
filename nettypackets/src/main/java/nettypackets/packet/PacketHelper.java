package nettypackets.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import nettypackets.ByteBufUtil;
import nettypackets.packetregistry.DefaultPacketRegistry;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.packetregistry.SidedPacketRegistryContainer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PacketHelper {

    public static ByteBuf toBytes(PacketRegistry registry, Packet p){
        return toBytes(Unpooled.buffer(32), registry, p);
    }

    public static ByteBuf toBytes(ByteBuf buf, PacketRegistry registry, Packet p){
        int startIndex = buf.writerIndex();
        buf.writerIndex(startIndex+8);

        ByteBufUtil.writeStringToBuf(registry.getRegistryName(), buf);
        p.writeBytes(buf);

        int size = buf.writerIndex()-startIndex-8;
        int lastIdx = buf.writerIndex();

        buf.writerIndex(startIndex);

        buf.writeInt(size);
        buf.writeInt(registry.getId(p.getClass()));

        buf.writerIndex(lastIdx);
        return buf;
    }

    public static List<Packet> handle(ByteBuf buf, ChannelHandlerContext ctx, SidedPacketRegistryContainer packetRegistries) throws Exception{
        Packet packet = handleOnePacket(buf, ctx, packetRegistries);
        if(packet==null) return Collections.emptyList();
        List<Packet> packets = new LinkedList<>();
        packets.add(packet);
        while((packet=handleOnePacket(buf, ctx, packetRegistries))!=null){
            packets.add(packet);
        }
        return packets;
    }

    private static Packet handleOnePacket(ByteBuf buf, ChannelHandlerContext ctx, SidedPacketRegistryContainer packetRegistries){
        if(buf.readableBytes()<4) return null;

        //get the size
        int size = buf.getInt(buf.readerIndex());

        if(buf.readableBytes()<size+8) return null;
        buf.skipBytes(4);

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
