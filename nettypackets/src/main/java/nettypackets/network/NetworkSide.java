package nettypackets.network;

import nettypackets.networkdata.NetworkData;

public interface NetworkSide {

    public NetworkData getNetworkData();

    public boolean isClient();

    public boolean isServer();

}
