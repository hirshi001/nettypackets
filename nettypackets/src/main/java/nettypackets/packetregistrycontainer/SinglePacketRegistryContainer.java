package nettypackets.packetregistrycontainer;

import nettypackets.packetregistry.DefaultPacketRegistry;
import nettypackets.packetregistry.PacketRegistry;

public class SinglePacketRegistryContainer implements PacketRegistryContainer{

    PacketRegistry registry = new DefaultPacketRegistry("");

    public SinglePacketRegistryContainer() {
        super();

    }

    @Override
    public PacketRegistry addRegistry(PacketRegistry registry) {
        throw new UnsupportedOperationException("adding registries is not supported");
    }

    @Override
    public PacketRegistry get(String name) {
        if(name.equals(registry.getRegistryName())) return registry;
        return null;
    }

    @Override
    public PacketRegistry getDefaultRegistry() {
        return registry;
    }

    @Override
    public boolean supportsMultipleRegistries() {
        return false;
    }
}
