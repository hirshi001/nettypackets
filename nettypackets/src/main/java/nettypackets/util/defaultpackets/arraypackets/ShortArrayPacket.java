package nettypackets.util.defaultpackets.arraypackets;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;

import java.util.Arrays;

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


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ShortArrayPacket)) return false;
        ShortArrayPacket packet = (ShortArrayPacket) obj;
        return Arrays.equals(array, packet.array);
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + "{ array=" + Arrays.toString(array) + "}";
    }
}