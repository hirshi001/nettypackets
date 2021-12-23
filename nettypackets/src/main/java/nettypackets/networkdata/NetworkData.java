package nettypackets.networkdata;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;
import nettypackets.packetdecoderencoder.PacketEncoderDecoder;
import nettypackets.packetregistrycontainer.PacketRegistryContainer;

public interface NetworkData {

    PacketEncoderDecoder getPacketEncoderDecoder();

    PacketRegistryContainer getPacketRegistryContainer();

    default Packet decode(ByteBuf in){
        Packet packet = getPacketEncoderDecoder().decode(getPacketRegistryContainer(), in);
        if(packet!=null)packet.networkData = this;
        return packet;
    }

    default void encode(Packet packet, ByteBuf out){
        if(!getPacketRegistryContainer().supportsMultipleRegistries()) packet.setPacketRegistry(getPacketRegistryContainer().getDefaultRegistry());
        getPacketEncoderDecoder().encode(packet, out);
    }

}
