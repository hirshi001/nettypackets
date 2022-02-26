package nettypackets.util.defaultpackets.primitivepackets;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;

public class FloatPacket extends Packet {

    public float value;

    public FloatPacket(){

    }

    public FloatPacket(float value){

    }

    @Override
    public void writeBytes(ByteBuf out) {
        super.writeBytes(out);
        out.writeFloat(value);
    }

    @Override
    public void readBytes(ByteBuf in) {
        super.readBytes(in);
        value = in.readFloat();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof FloatPacket)) return false;
        FloatPacket packet = (FloatPacket) obj;
        return packet.value == value;
    }

    @Override
    public String toString() {
        return "FloatPacket{" +
                "value=" + value +
                '}';
    }
}
