package nettypackets.util.defaultpackets.arraypackets;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;

import java.util.Arrays;

public class ByteArrayPacket extends Packet {
    public byte[] array;

    public ByteArrayPacket() {
        super();
    }

    public ByteArrayPacket(byte[] array) {
        super();
        this.array = array;
    }

    @Override
    public void readBytes(ByteBuf buf) {
        super.readBytes(buf);
        array = new byte[buf.readInt()];
        for (int i = 0; i < array.length; i++) {
            array[i] = buf.readByte();
        }
    }

    @Override
    public void writeBytes(ByteBuf buf) {
        super.writeBytes(buf);
        buf.writeInt(array.length);
        for (int i = 0; i < array.length; i++) {
            buf.writeByte(array[i]);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ByteArrayPacket)) return false;
        ByteArrayPacket packet = (ByteArrayPacket) obj;
        return Arrays.equals(array, packet.array);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{ array=" + Arrays.toString(array) + "}";
    }

}
