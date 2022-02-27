package nettypackets.packetdecoderencoder;

import io.netty.buffer.ByteBuf;
import nettypackets.network.packethandlercontext.PacketHandlerContext;
import nettypackets.packet.Packet;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.packetregistrycontainer.PacketRegistryContainer;
import org.jetbrains.annotations.Nullable;


public interface PacketEncoderDecoder {

    /**
     * Decodes a single packet from the given ByteBuf and returns it.
     * It should set the PacketRegistry and PacketHandler of the PacketHandlerContext in the Packet.
     * NetworkData should be set by the NetworkData decode method.
     * @param container the SidedPacketRegistryContainer which contains the packet registries
     * @param in the ByteBuf to read from
     * @return the decoded packet or null if the packet could not be decoded because there were not enough bytes
     */
    public PacketHandlerContext<?> decode(PacketRegistryContainer container, ByteBuf in, @Nullable PacketHandlerContext<?> context);

    /**
     * Encodes a single packet into the given ByteBuf.
     * @param packet the packet to encode
     * @param out the ByteBuf to write to
     */
    public void encode(Packet packet, PacketRegistryContainer container, PacketRegistry packetRegistry, ByteBuf out);

}
