# NettyPackets
What is this? It's a networking library built on top of Netty to make communication between devices easier. NettyPackets makes it easy to send Packets of information from a client to server, from a server to multiple clients, and handle those packets when they arrive at their destination. Before using, it is suggested that you know how to use Netty at least a little bit. Check out their website! https://netty.io/

## Example
```java
import nettypackets.network.clientfactory.ClientFactory;

public class MainClient{  
    
    public static PacketRegistry clientRegistry;
    public static Client client;
    public static PacketEncoderDecoder encoderDecoder;
    public static ClientFactory clientFactory;

    public static void main(String[] args) {
        encoderDecoder = new SimplePacketEncodeDecoder();
        
        clientFactory = ClientFactory.singlePacketRegistryClientFactory().
                setIpAddress("localhost").
                setPort(8080).
                setPacketEncoderDecoder(encoderDecoder).
                setBootstrap(new Bootstrap().                   //some settings taken from Netty's user guide
                        group(new NioEventLoopGroup()).         //^
                        channel(NioSocketChannel.class).        //^
                        option(ChannelOption.SO_KEEPALIVE, true)//^
                ).
                addPacketRegistry(clientRegistry = new DefaultPacketRegistry(NAME)
                        .register(new PacketHolder<>(TestPacket::new, TestPacket::clientHandle, TestPacket.class), 0)
                        .register(new PacketHolder<>(TestPacket2::new, TestPacket2::clientHandle, TestPacket2.class), 1)
                        .registerDefaultArrayPrimitivePackets()
                        .registerDefaultObjectPackets()
                        .registerDefaultPrimitivePackets()
                );
        
        //add ClientListeners to the clientFactory
        clientFactory.addListener(new AbstractClientListener() {)
            @Override
            public void onConnected(Client client) {
                System.out.println("Connected!");
            }

            @Override
            public void onDisconnected(Client client) {
                System.out.println("Disconnected!");
            }

            @Override
            public void onException(Client client, Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        });
        
        
        //different ways to connect client to server
        //1
        client = clientFactory.connectNowUninterruptibly();
        //2
        client = clientFactory.conn
        
    }
    
    
}

```

# Usage
## Setup
First, you must create a `PacketEncoderDecoder`(encoderdecoder) object, which is in charge of encoding/decoding your packets becfore they are send/received by a networking side. The encoderdecoder must be the same on both the client and server so if the client and server are in different projects, make sure the encoderdeocder objects in both projects are instances of the same class.
<br>
A `PacketEncoderDecoder` class is provided called `SimplePacketEncoderDecoder`. If you want, you may make your own `PacketEncoderdecoder` and use that instead, but it must implement the `PacketEncoderDecoder` interface.
```java
PacketEncoderDecoder encoderDecoder = new SimplePacketEncoderDecoder(); //new MyPacketEncoderDecoder();
```

## Creating a Server
To create a `Server` , you must first create a `ServerFactory`
```java
ServerFactory serverFactory = ServerFactory.multiPacketRegistryServerFactory()
```
or
```java
ServerFactory serverFactory = ServerFactory.singlePacketRegistryServerFactory()
```
What's the difference? The first one allows you to register multiple Packet Registries while the second one does not allow you to register Packet Registries and will provide one for you. Once we created the factory, we must set some values in the factory. The factory has the following methods which you should use:
- `setPort`
- `setPacketEncoderDecoder`
- `setBootstrap`
- `addPacketRegistry`
- `getPacketRegistryContainer`

Most of these methods return a `ServerFactory` for chaining.
