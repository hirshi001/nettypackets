package nettypackets.packetdecoderencoder;

import io.netty.buffer.ByteBuf;
import nettypackets.network.packethandlercontext.PacketHandlerContext;
import nettypackets.packet.Packet;
import nettypackets.packet.PacketHolder;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.packetregistrycontainer.PacketRegistryContainer;
import org.jetbrains.annotations.Nullable;


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
    public PacketHandlerContext<?> decode(PacketRegistryContainer container, ByteBuf in, @Nullable PacketHandlerContext context) {
        if(in.readableBytes()<8) return null; // If there is not enough bytes to read the length and the id


        int size = in.getInt(in.readerIndex()); // Get the size of the packet without changing the reader index
        if(size>maxSize) throw new IllegalArgumentException("Packet size is too big"); // If the size is too big

        if(in.readableBytes()<size+8) return null; // If there is not enough bytes to read the packet
        in.readInt(); // Read the size of the packet (we already know it)

        int id = in.readInt();
        ByteBuf msg = in.readBytes(size); // Read the packet

        int registryId = msg.readInt();
        PacketRegistry registry = container.get(registryId);
        if(registry==null) throw new NullPointerException("The registry id " + registryId + " does not exist in the SidedPacketRegistryContainer " + container);

        PacketHolder holder = registry.getPacketHolder(id);

        Packet packet = holder.getPacket();
        packet.readBytes(msg);
        msg.release();

        if(context==null) context = new PacketHandlerContext<>();
        context.packetHandler = holder.handler;
        context.packetRegistry = registry;
        context.packet = packet;

        return context;
    }

    @Override
    public void encode(Packet packet, PacketRegistryContainer container, PacketRegistry packetRegistry, ByteBuf out) {

        int startIndex = out.writerIndex(); // start index
        out.writerIndex(startIndex+8); // Reserve space for the length and the id

        out.writeInt(packetRegistry.getId()); // Write the id of the registry
        packet.writeBytes(out); // Write the packet

        int lastIdx = out.writerIndex(); // Get the last index
        int size = lastIdx-startIndex-8; // Calculate the size of packet not including the length and id

        out.writerIndex(startIndex); // Set the writer index back to the start index

        out.writeInt(size); // Write the size of the packet
        out.writeInt(packetRegistry.getId(packet.getClass())); // Write the id of the packet

        out.writerIndex(lastIdx); // Set the writer index back to the last index
    }

}
