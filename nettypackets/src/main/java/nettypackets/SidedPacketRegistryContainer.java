package nettypackets;

import java.util.HashMap;
import java.util.Map;

public class SidedPacketRegistryContainer {

    public SidedPacketRegistryContainer(){

    }

    private final Map<String, PacketRegistry> packetRegistryMap = new HashMap<>();

    public PacketRegistry create(String registryName){
        if(packetRegistryMap.containsKey(registryName)){
            throw new IllegalArgumentException("The registry name [" + registryName + "] is already in use!");
        }
        PacketRegistry registry = new PacketRegistry(registryName);
        packetRegistryMap.put(registryName, registry);
        return registry;
    }

    public PacketRegistry get(String name){
        return packetRegistryMap.get(name);
    }


}
