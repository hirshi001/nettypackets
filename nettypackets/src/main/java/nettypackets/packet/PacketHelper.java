package nettypackets.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import nettypackets.ByteBufUtil;
import nettypackets.PacketHandlerContext;
import nettypackets.packetregistry.DefaultPacketRegistry;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.packetregistry.SidedPacketRegistryContainer;

import java.util.*;

public class PacketHelper {


    /*
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

    private static Packet handleOnePacket(ByteBuf buf, ChannelHandlerContext ctx, SidedPacketRegistryContainer packetRegistries) throws Exception{
        if(buf.readableBytes()<8) return null;

        //get the size
        int size = buf.getInt(buf.readerIndex());

        if(buf.readableBytes()<size+8) return null;
        buf.readInt(); //get the size bytes outta here

        //get the id
        int id = buf.readInt();
        ByteBuf msg = buf.readBytes(size);

        //get the registry
        String namespace = ByteBufUtil.readStringFromBuf(msg);
        PacketRegistry registry = packetRegistries.get(namespace);

        //get the packet holder
        PacketHolder<? extends Packet> holder = registry.getPacketHolder(id);
        PacketHandlerContext packetContext = new PacketHandlerContext();
        packetContext.set(ctx, registry, packetRegistries);
        return holder.handlePacket(msg, ctx, size, packetContext);
    }


    public static void handle(ByteBuf buf, ChannelHandlerContext ctx, SidedPacketRegistryContainer packetRegistries, Collection<Packet> packetsHandledCollection) throws Exception{
        Packet packet;
        if(packetsHandledCollection==null){
            while(handleOnePacket(buf, ctx, packetRegistries) !=null);
        }
        else{
            while((packet=handleOnePacket(buf, ctx, packetRegistries))!=null) {
                packetsHandledCollection.add(packet);
            }
        }
    }
     */


}
