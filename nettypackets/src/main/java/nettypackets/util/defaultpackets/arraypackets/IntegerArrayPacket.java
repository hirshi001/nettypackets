package nettypackets.util.defaultpackets.arraypackets;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;

import java.util.Arrays;

public class IntegerArrayPacket extends Packet {

    public int[] array;

    public IntegerArrayPacket() {
        super();
    }

    public IntegerArrayPacket(int[] array) {
        super();
        this.array = array;
    }

    @Override
    public void writeBytes(ByteBuf out) {
        super.writeBytes(out);
        out.writeInt(array.length);
        for (int j : array) {
            out.writeInt(j);
        }
    }

    @Override
    public void readBytes(ByteBuf in) {
        super.readBytes(in);
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            array[i] = in.readInt();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof IntegerArrayPacket)) return false;
        IntegerArrayPacket packet = (IntegerArrayPacket) obj;
        return Arrays.equals(array, packet.array);
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + "{ array=" + Arrays.toString(array) + "}";
    }
}
