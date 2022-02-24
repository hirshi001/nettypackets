package nettypackets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import nettypackets.packet.Packet;
import nettypackets.packetregistry.PacketRegistry;

import java.nio.charset.StandardCharsets;

public class TestPacket2 extends Packet {


    public String message;

    public TestPacket2() {
        super();
    }

    public TestPacket2(String message){
        this.message = message;
    }

    @Override
    public void writeBytes(ByteBuf out) {
        super.writeBytes(out);
        byte[] msgBytes = message.getBytes(StandardCharsets.UTF_8);
        out.writeInt(msgBytes.length);
        out.writeBytes(msgBytes);
    }

    @Override
    public void readBytes(ByteBuf in) {
        super.readBytes(in);
        int length = in.readInt();
        byte[] msgBytes = new byte[length];
        in.readBytes(msgBytes);
        message = new String(msgBytes, StandardCharsets.UTF_8);
    }

    public void serverHandle(){
        System.out.println("[Client -> Server]: " + message);
        //LibraryTest.server.sendPacketToAll(this.setResponsePacket(this));
    }

    public void clientHandle(){
        System.out.println("[Server -> Client]: " + message);
        //LibraryTest.client.sendPacket(new TestPacket2("Test packet 2: client responding to server").setResponsePacket(this).setPacketRegistry(LibraryTest.clientRegistry));
    }
}
