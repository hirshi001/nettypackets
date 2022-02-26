package nettypackets.util.defaultpackets.primitivepackets;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;

public class LongPacket extends Packet {

    long value;

    public LongPacket(){

    }

    public LongPacket(long value){
        this.value = value;
    }

    @Override
    public void writeBytes(ByteBuf out) {
        super.writeBytes(out);
        out.writeLong(value);
    }

    @Override
    public void readBytes(ByteBuf in) {
        super.readBytes(in);
        value = in.readLong();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof LongPacket)) return false;
        LongPacket packet = (LongPacket) obj;
        return packet.value == value;
    }

    @Override
    public String toString() {
        return "LongPacket{" +
                "value=" + value +
                '}';
    }
}
