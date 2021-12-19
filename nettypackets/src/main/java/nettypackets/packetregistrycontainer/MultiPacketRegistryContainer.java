package nettypackets.packetregistrycontainer;

import nettypackets.packetregistry.DefaultPacketRegistry;
import nettypackets.packetregistry.PacketRegistry;

import java.util.HashMap;
import java.util.Map;

public class MultiPacketRegistryContainer implements PacketRegistryContainer{


    private final Map<String, PacketRegistry> packetRegistryMap = new HashMap<>();
    private final PacketRegistry defaultRegistry;

    public MultiPacketRegistryContainer(){
        defaultRegistry = new DefaultPacketRegistry("default");
        addRegistry(defaultRegistry);
    }

    public PacketRegistry addRegistry(PacketRegistry registry){
        String registryName = registry.getRegistryName();
        if(packetRegistryMap.containsKey(registryName)){
            throw new IllegalArgumentException("The registry name [" + registryName + "] is already in use!");
        }
        packetRegistryMap.put(registryName, registry);
        return registry;
    }

    public PacketRegistry get(String name){
        return packetRegistryMap.get(name);
    }

    public PacketRegistry getDefaultRegistry(){
        return defaultRegistry;
    }

    public PacketRegistry newRegistry(String registryName){
        return addRegistry(new DefaultPacketRegistry(registryName));
    }

    public boolean supportsMultipleRegistries(){
        return true;
    }


}
