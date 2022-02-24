package example;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;
import nettypackets.util.defaultpackets.primitivepackets.IntegerPacket;

import java.util.concurrent.ThreadLocalRandom;

public class RequestRandomIntegerPacket extends Packet {

    public int min, max;

    public RequestRandomIntegerPacket() {
        super();
    }

    public RequestRandomIntegerPacket(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void writeBytes(ByteBuf out) {
        super.writeBytes(out);
        out.writeInt(min);
        out.writeInt(max);
    }

    @Override
    public void readBytes(ByteBuf in) {
        super.readBytes(in);
        min = in.readInt();
        max = in.readInt();
    }

    public static void handleOnServer(RequestRandomIntegerPacket packet) {
        Packet packetToSend = new IntegerPacket(ThreadLocalRandom.current().nextInt(packet.min, packet.max));
        packetToSend.setResponsePacket(packet); //since we are responding, we must set which packet our packet is responding to

        //send the packet back to the client
        //MainServer.server.sendPacket(packetToSend, packet.packetHandlerContext.channelHandlerContext.channel());
    }

}
