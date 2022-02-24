package nettypackets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import nettypackets.network.client.UDPClient;
import nettypackets.network.packethandlercontext.PacketHandlerContext;
import nettypackets.network.packethandlercontext.PacketType;
import nettypackets.network.server.IServer;
import nettypackets.network.server.TCPServer;
import nettypackets.network.server.UDPServer;
import nettypackets.packet.Packet;
import nettypackets.util.defaultpackets.primitivepackets.DoublePacket;
import nettypackets.util.defaultpackets.primitivepackets.IntegerPacket;
import nettypackets.util.defaultpackets.primitivepackets.StringPacket;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class TestPacket extends Packet {

    static AtomicInteger testPacketClientHandleCounter = new AtomicInteger(0);
    static AtomicInteger testPacketServerHandleCounter = new AtomicInteger(0);



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

    public static void serverHandle(PacketHandlerContext<TestPacket> packetHandlerContext){
        //packetHandlerContext.ctx.writeAndFlush(new Pair<>(new StringPacket("HI").setResponsePacket(packetHandlerContext.packet), packetHandlerContext.packetRegistry));
        if(packetHandlerContext.packetType == PacketType.UDP){
            ((UDPServer)packetHandlerContext.networkSide).send(new StringPacket("HI").setResponsePacket(packetHandlerContext.packet), packetHandlerContext.packetRegistry, packetHandlerContext.source);
        }else{
            ((TCPServer)packetHandlerContext.networkSide).send(new StringPacket("HI").setResponsePacket(packetHandlerContext.packet), packetHandlerContext.packetRegistry, packetHandlerContext.ctx.channel()).syncUninterruptibly();
        }
    }

    public static void clientHandle(PacketHandlerContext<TestPacket> packetHandlerContext){
        if(packetHandlerContext.packetType==PacketType.UDP){
            ((UDPClient)packetHandlerContext.networkSide).send(new DoublePacket(Math.random()).setResponsePacket(packetHandlerContext.packet), packetHandlerContext.packetRegistry);
        }
    }
}
