package nettypackets.util.defaultpackets.primitivepackets;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;

public class IntegerPacket extends Packet {

    public int value;

    public IntegerPacket(){

    }

    public IntegerPacket(int value){
        this.value = value;
    }

    @Override
    public void writeBytes(ByteBuf out) {
        super.writeBytes(out);
        out.writeInt(value);
    }

    @Override
    public void readBytes(ByteBuf in) {
        super.readBytes(in);
        value = in.readInt();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof IntegerPacket)) return false;
        IntegerPacket packet = (IntegerPacket) obj;
        return packet.value == value;
    }

    @Override
    public String toString() {
        return "IntegerPacket{" +
                "value=" + value +
                '}';
    }
}
