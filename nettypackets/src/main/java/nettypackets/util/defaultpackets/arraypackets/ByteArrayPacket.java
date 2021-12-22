package nettypackets.util.defaultpackets.arraypackets;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;

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

}
