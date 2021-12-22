package nettypackets.util.defaultpackets.arraypackets;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;

public class ShortArrayPacket extends Packet {
    public short[] array;

    public ShortArrayPacket() {
        super();
    }

    public ShortArrayPacket(short[] array) {
        super();
        this.array = array;
    }

    @Override
    public void readBytes(ByteBuf buf) {
        super.readBytes(buf);
        array = new short[buf.readInt()];
        for (int i = 0; i < array.length; i++) {
            array[i] = buf.readShort();
        }
    }

    @Override
    public void writeBytes(ByteBuf buf) {
        super.writeBytes(buf);
        buf.writeInt(array.length);
        for (int i = 0; i < array.length; i++) {
            buf.writeShort(array[i]);
        }
    }
}