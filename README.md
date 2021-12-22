# NettyPackets
What is this? It's a networking library built on top of Netty to make communication between devices easier. NettyPackets makes it easy to send Packets of information from a client to server, from a server to multiple clients, and handle those packets when they arrive at their destination. Before using, it is suggested that you know how to use Netty at least a little bit. Check out their website! https://netty.io/


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
