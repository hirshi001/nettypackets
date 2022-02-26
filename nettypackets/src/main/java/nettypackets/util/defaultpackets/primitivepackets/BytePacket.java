package nettypackets.util.defaultpackets.primitivepackets;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;

public class BytePacket extends Packet {

    public byte value;

    public BytePacket(byte value) {
        this.value = value;
    }

    @Override
    public void writeBytes(ByteBuf out) {
        super.writeBytes(out);
        out.writeByte(value);
    }

    @Override
    public void readBytes(ByteBuf in) {
        super.readBytes(in);
        value = in.readByte();
    }

    public BytePacket(){

    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof BytePacket)) return false;
        BytePacket packet = (BytePacket) obj;
        return packet.value == value;
    }


    @Override
    public String toString() {
        return "BytePacket{" +
                "value=" + value +
                '}';
    }
}
