package nettypackets.networkdata;

import nettypackets.packetdecoderencoder.PacketEncoderDecoder;
import nettypackets.packetregistry.SidedPacketRegistryContainer;

public class DefaultNetworkData implements NetworkData{

    protected PacketEncoderDecoder encoderDecoder;
    protected SidedPacketRegistryContainer registryContainer;

    public DefaultNetworkData(PacketEncoderDecoder encoderDecoder, SidedPacketRegistryContainer registryContainer) {
        this.encoderDecoder = encoderDecoder;
        this.registryContainer = registryContainer;
    }

    @Override
    public PacketEncoderDecoder getPacketEncoderDecoder() {
        return encoderDecoder;
    }

    @Override
    public SidedPacketRegistryContainer getPacketRegistryContainer() {
        return registryContainer;
    }

}
