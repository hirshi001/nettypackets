package nettypackets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import nettypackets.packet.Packet;

import java.nio.charset.StandardCharsets;

public class TestPacket extends Packet {

    public String message;

    public TestPacket() {
        super();
    }

    public TestPacket(String message){
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

    public void serverHandle(ChannelHandlerContext ctx){
        LibraryTest.server.sendPacketToAllConnected(LibraryTest.serverRegistry, this);
        System.out.println("[Client -> Server]: " + message);
    }

    public void clientHandle(ChannelHandlerContext ctx){
        System.out.println("[Server -> Client]: " + message);
    }
}
