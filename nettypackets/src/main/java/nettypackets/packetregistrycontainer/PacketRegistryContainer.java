package nettypackets.packetregistrycontainer;

import nettypackets.packetregistry.DefaultPacketRegistry;
import nettypackets.packetregistry.PacketRegistry;

import java.util.Collection;

/**
 * @author Hrishikesh Ingle
 * Inteface for classes which store packet registries
 * The default registry will typically have an integer id of 0 and name of "default"
 */
public interface PacketRegistryContainer {

    public static final String DEFAULT_REGISTRY_NAME = "default";


    /**
     * Adds a PacketRegistry to this PacketRegistryContainer if this PacketRegistryContainer supports multiple PacketRegistries
     * @param registry
     * @return
     */
    public PacketRegistry addRegistry(PacketRegistry registry);

    /**
     * Gets the PacketRegistry with the given name
     * @param name
     * @return the PacketRegistry with the given name, or null if no PacketRegistry with the given name exists
     */
    public PacketRegistry get(String name);

    /**
     *
     * @return the default PacketRegistry created when this PacketRegistryContainer is created
     */
    public PacketRegistry getDefaultRegistry();

    /**
     * Creates a new PacketRegistry which is then added to this PacketRegistryContainer
     * The method {@link PacketRegistryContainer#supportsMultipleRegistries()} must return true for this method to work
     * @param registryName
     * @return the newly created PacketRegistry
     * @throws UnsupportedOperationException if this PacketRegistryContainer does not support multiple PacketRegistries
     */
    default PacketRegistry newRegistry(String registryName){
        if(supportsMultipleRegistries()) return addRegistry(new DefaultPacketRegistry(registryName));
        else throw new UnsupportedOperationException("This container does not support multiple registries");
    }

    /**
     *
     * @return true if this PacketRegistryContainer supports multiple PacketRegistries
     */
    public boolean supportsMultipleRegistries();

    public void setPacketRegistryID(PacketRegistry registry, int id);

    public PacketRegistry get(int id);

    public Collection<PacketRegistry> registries();
}
