package nettypackets.util.defaultpackets.arraypackets;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;

import java.util.Arrays;

public class FloatArrayPacket extends Packet {
    public float[] array;

    public FloatArrayPacket() {
        super();
    }

    public FloatArrayPacket(float[] array) {
        super();
        this.array = array;
    }

    @Override
    public void readBytes(ByteBuf buf) {
        super.readBytes(buf);
        int length = buf.readInt();
        array = new float[length];
        for (int i = 0; i < length; i++) {
            array[i] = buf.readFloat();
        }
    }

    @Override
    public void writeBytes(ByteBuf buf) {
        super.writeBytes(buf);
        buf.writeInt(array.length);
        for (int i = 0; i < array.length; i++) {
            buf.writeFloat(array[i]);
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof FloatArrayPacket)) return false;
        FloatArrayPacket packet = (FloatArrayPacket) obj;
        return Arrays.equals(array, packet.array);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{ array=" + Arrays.toString(array) + "}";
    }
}