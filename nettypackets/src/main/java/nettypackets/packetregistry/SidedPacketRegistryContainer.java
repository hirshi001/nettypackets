package nettypackets.packetregistry;

import java.util.HashMap;
import java.util.Map;

public class SidedPacketRegistryContainer {

    public SidedPacketRegistryContainer(){

    }

    private final Map<String, PacketRegistry> packetRegistryMap = new HashMap<>();

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


}
