package example;

import nettypackets.network.client.IClient;
import nettypackets.packetdecoderencoder.PacketEncoderDecoder;
import nettypackets.packetregistry.PacketRegistry;

public class MainClient {

    public static PacketEncoderDecoder encoderDecoder;
    public static volatile IClient client;

    public static PacketRegistry defaultRegistry;

    public static void main(String[] args) {

    }

}
