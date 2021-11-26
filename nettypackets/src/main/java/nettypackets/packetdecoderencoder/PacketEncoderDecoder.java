package nettypackets.packetdecoderencoder;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.packetregistry.SidedPacketRegistryContainer;

public interface PacketEncoderDecoder {

    PacketEncoderDecoder DEFAULT_ENCODER_DECODER = new SimplePacketEncoderDecoder();

    /**
     * Decodes a single packet from the given ByteBuf and returns it.
     * @param container the SidedPacketRegistryContainer which contains the packet registries
     * @param in the ByteBuf to read from
     * @return the decoded packet or null if the packet could not be decoded because there were not enough bytes
     */
    public Packet decode(SidedPacketRegistryContainer container, ByteBuf in);

    /**
     * Encodes a single packet into the given ByteBuf.
     * @param packet the packet to encode
     * @param registry the PacketRegistry which contains the packet
     * @param out the ByteBuf to write to
     */
    public void encode(Packet packet, PacketRegistry registry, ByteBuf out);

}
