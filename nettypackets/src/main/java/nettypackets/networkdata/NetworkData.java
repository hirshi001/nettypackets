package nettypackets.networkdata;

import io.netty.buffer.ByteBuf;
import nettypackets.network.packethandlercontext.PacketHandlerContext;
import nettypackets.packet.Packet;
import nettypackets.packetdecoderencoder.PacketEncoderDecoder;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.packetregistrycontainer.PacketRegistryContainer;

public interface NetworkData {

    PacketEncoderDecoder getPacketEncoderDecoder();

    PacketRegistryContainer getPacketRegistryContainer();

    default PacketHandlerContext<?> decode(ByteBuf in){
        PacketHandlerContext<?> context = getPacketEncoderDecoder().decode(getPacketRegistryContainer(), in, null);
        if(context!=null){
            context.networkData = this;
        }
        return context;
    }

    default void encode(Packet packet, PacketRegistry packetRegistry, ByteBuf out){
        if(!getPacketRegistryContainer().supportsMultipleRegistries()){
            packetRegistry = getPacketRegistryContainer().getDefaultRegistry();
        }
        getPacketEncoderDecoder().encode(packet, getPacketRegistryContainer(), packetRegistry, out);
    }

}
