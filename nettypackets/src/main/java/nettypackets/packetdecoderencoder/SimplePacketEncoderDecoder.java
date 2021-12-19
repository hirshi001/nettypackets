package nettypackets.packetdecoderencoder;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;
import nettypackets.packet.PacketHandler;
import nettypackets.packet.PacketHolder;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.packetregistrycontainer.PacketRegistryContainer;
import nettypackets.util.ByteBufUtil;

public class SimplePacketEncoderDecoder implements PacketEncoderDecoder {

    public int maxSize;

    public SimplePacketEncoderDecoder(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    public SimplePacketEncoderDecoder(){
        this(2048);
    }

    @Override
    public Packet decode(PacketRegistryContainer container, ByteBuf in) {
        if(in.readableBytes()<8) return null; // If there is not enough bytes to read the length and the id


        int size = in.getInt(in.readerIndex()); // Get the size of the packet without changing the reader index
        if(size>maxSize) throw new IllegalArgumentException("Packet size is too big"); // If the size is too big

        if(in.readableBytes()<size+8) return null; // If there is not enough bytes to read the packet
        in.readInt(); // Read the size of the packet (we already know it)

        int id = in.readInt();
        ByteBuf msg = in.readBytes(size); // Read the packet

        String registryName = ByteBufUtil.readStringFromBuf(msg);
        PacketRegistry registry = container.get(registryName);
        if(registry==null) throw new NullPointerException("The registry name " + registryName + " does not exist in the SidedPacketRegistryContainer " + container);

        PacketHolder<? extends Packet> holder = registry.getPacketHolder(id);

        Packet packet = holder.getPacket();
        packet.readBytes(msg);

        packet.packetHandlerContext.packetHandler = (PacketHandler<Packet>) holder.handler; //should work
        packet.packetHandlerContext.packetRegistry = registry;


        return packet;
    }

    @Override
    public void encode(Packet packet, ByteBuf out) {
        PacketRegistry registry = packet.packetHandlerContext.packetRegistry;

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
