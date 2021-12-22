package nettypackets.util.defaultpackets.arraypackets;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;

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

}