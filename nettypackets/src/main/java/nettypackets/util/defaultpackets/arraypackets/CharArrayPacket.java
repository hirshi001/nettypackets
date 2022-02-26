package nettypackets.util.defaultpackets.arraypackets;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;

import java.util.Arrays;

public class CharArrayPacket extends Packet {

    public char[] array;

    public CharArrayPacket() {
        super();
    }

    public CharArrayPacket(char[] array) {
        super();
        this.array = array;
    }

    @Override
    public void writeBytes(ByteBuf out) {
        super.writeBytes(out);
        out.writeInt(array.length);
        for (int j : array) {
            out.writeChar(j);
        }
    }

    @Override
    public void readBytes(ByteBuf in) {
        super.readBytes(in);
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            array[i] = in.readChar();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof CharArrayPacket)) return false;
        CharArrayPacket packet = (CharArrayPacket) obj;
        return Arrays.equals(array, packet.array);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{ array=" + Arrays.toString(array) + "}";
    }
}
