package nettypackets.network.clientfactory;

import io.netty.bootstrap.Bootstrap;
import nettypackets.network.client.Client;
import nettypackets.network.client.DefaultClient;
import nettypackets.networkdata.DefaultNetworkData;
import nettypackets.networkdata.NetworkData;
import nettypackets.packetdecoderencoder.PacketEncoderDecoder;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.packetregistrycontainer.MultiPacketRegistryContainer;
import nettypackets.packetregistrycontainer.PacketRegistryContainer;
import nettypackets.packetregistrycontainer.SinglePacketRegistryContainer;

public class ClientFactory {

    private String ipAddress = "localhost";
    private int port = 8080;
    private PacketEncoderDecoder packetEncoderDecoder;
    private Bootstrap bootstrap;
    private final PacketRegistryContainer packetRegistryContainer;
    private final boolean isMultiPacketRegistry;


    public static ClientFactory multiPacketRegistryClientFactory(){
        return new ClientFactory(new MultiPacketRegistryContainer(), true);
    }

    public static ClientFactory singlePacketRegistryClientFactory(){
        return new ClientFactory(new SinglePacketRegistryContainer(), false);
    }

    private ClientFactory(PacketRegistryContainer packetRegistryContainer, boolean isMultiPacketRegistry) {
        this.packetRegistryContainer = packetRegistryContainer;
        this.isMultiPacketRegistry = isMultiPacketRegistry;
    }

    public boolean isMultiPacketRegistry() {
        return isMultiPacketRegistry;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public ClientFactory setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public int getPort() {
        return port;
    }

    public ClientFactory setPort(int port) {
        this.port = port;
        return this;
    }

    public PacketEncoderDecoder getPacketEncoderDecoder() {
        return packetEncoderDecoder;
    }

    public ClientFactory setPacketEncoderDecoder(PacketEncoderDecoder packetEncoderDecoder) {
        this.packetEncoderDecoder = packetEncoderDecoder;
        return this;
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public ClientFactory setBootstrap(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
        return this;
    }

    public ClientFactory addPacketRegistry(PacketRegistry packetRegistry){
        this.packetRegistryContainer.addRegistry(packetRegistry);
        return this;
    }

    public Client connectClientNow() throws InterruptedException {
        Client client = new ClientBuilder().build();
        client.connect(bootstrap).sync();
        return client;
    }

    public Client connectClientNowUninterruptibly(){
        Client client =  new ClientBuilder().build();
        client.connect(bootstrap).syncUninterruptibly();
        return client;
    }

    public ClientConnectFuture connectAsync(){
        Client client =  new ClientBuilder().build();
        return new ClientConnectFuture(client, bootstrap);
    }

    private class ClientBuilder{

        Bootstrap bootstrap;
        NetworkData networkData;


        public ClientBuilder(){

            networkData = getNetworkData();

            if(ClientFactory.this.bootstrap != null){
                bootstrap = ClientFactory.this.bootstrap;
            }else{
                bootstrap = new Bootstrap();
            }
        }

        public Client build(){
            return new DefaultClient(ipAddress, port, networkData);
        }


        private NetworkData getNetworkData(){
            return new DefaultNetworkData(packetEncoderDecoder, packetRegistryContainer);
        }


    }




}
