package nettypackets.util.defaultpackets.arraypackets;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;

public class LongArrayPacket extends Packet {
    public long[] array;

    public LongArrayPacket() {
        super();
    }

    public LongArrayPacket(long[] array) {
        super();
        this.array = array;
    }

    @Override
    public void readBytes(ByteBuf buf) {
        super.readBytes(buf);
        int size = buf.readInt();
        array = new long[size];
        for (int i = 0; i < size; i++) {
            array[i] = buf.readLong();
        }
    }

    @Override
    public void writeBytes(ByteBuf buf) {
        super.writeBytes(buf);
        buf.writeInt(array.length);
        for (long i : array) {
            buf.writeLong(i);
        }
    }
}