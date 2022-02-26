package nettypackets.util.defaultpackets.primitivepackets;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;

public class ShortPacket extends Packet {

    public short value;

    public ShortPacket(){

    }

    public ShortPacket(short value){
        this.value = value;
    }

    @Override
    public void writeBytes(ByteBuf out) {
        super.writeBytes(out);
        out.writeShort(value);
    }

    @Override
    public void readBytes(ByteBuf in) {
        super.readBytes(in);
        value = in.readShort();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ShortPacket)) return false;
        ShortPacket packet = (ShortPacket) obj;
        return packet.value == value;
    }

    @Override
    public String toString() {
        return "ShortPacket{" +
                "value=" + value +
                '}';
    }
}
