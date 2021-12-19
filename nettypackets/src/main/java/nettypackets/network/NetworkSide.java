package nettypackets.network;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import nettypackets.networkdata.NetworkData;

import java.util.concurrent.Future;

public interface NetworkSide<B extends AbstractBootstrap<?,?>> {

    public NetworkData getNetworkData();

    public boolean isClient();

    public boolean isServer();

    public ChannelFuture connect(B boostrap);

}
