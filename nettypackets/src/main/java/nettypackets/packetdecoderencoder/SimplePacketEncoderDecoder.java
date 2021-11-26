package nettypackets.packetdecoderencoder;

import io.netty.buffer.ByteBuf;
import nettypackets.ByteBufUtil;
import nettypackets.PacketHandlerContext;
import nettypackets.packet.Packet;
import nettypackets.packet.PacketHolder;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.packetregistry.SidedPacketRegistryContainer;

import java.util.NoSuchElementException;

public class SimplePacketEncoderDecoder implements PacketEncoderDecoder {

    public SimplePacketEncoderDecoder() {
        super();
    }

    @Override
    public Packet decode(SidedPacketRegistryContainer container, ByteBuf in) {
        if(in.readableBytes()<8) return null; // If there is not enough bytes to read the length and the id


        int size = in.getInt(in.readerIndex()); // Get the size of the packet

        if(in.readableBytes()<size+8) return null; // If there is not enough bytes to read the packet
        in.readInt(); // Read the size of the packet (we already know it)

        int id = in.readInt(); // Read the id of the packet
        ByteBuf msg = in.readBytes(size); // Read the packet

        String registryName = ByteBufUtil.readStringFromBuf(msg); // Read the registry name
        PacketRegistry registry = container.get(registryName); // Get the registry
        if(registry==null) throw new NullPointerException("The registry name " + registryName + " does not exist in the SidedPacketRegistryContainer " + container);

        PacketHolder<? extends Packet> holder = registry.getPacketHolder(id); // Get the packet holder

        Packet packet = holder.getPacket(msg); // Get the packet
        return packet;
    }

    @Override
    public void encode(Packet packet, PacketRegistry registry, ByteBuf out) {
        int startIndex = out.writerIndex(); // start index
        out.writerIndex(startIndex+8); // Reserve space for the length and the id

        ByteBufUtil.writeStringToBuf(registry.getRegistryName(), out); // Write the registry name
        packet.writeBytes(out); // Write the packet

        int lastIdx = out.writerIndex(); // Get the last index
        int size = lastIdx-startIndex-8; // Calculate the size of packet not including the length and id

        out.writerIndex(startIndex); // Set the writer index back to the start index

        out.writeInt(size); // Write the size of the packet
        out.writeInt(registry.getId(packet.getClass())); // Write the id of the packet

        out.writerIndex(lastIdx); // Set the writer index back to the last index
    }

}
