package nettypackets.util.defaultpackets.primitivepackets;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;

public class DoublePacket extends Packet {

    public double value;

    public DoublePacket(){

    }

    public DoublePacket(double value){
        this.value = value;
    }

    @Override
    public void writeBytes(ByteBuf out) {
        super.writeBytes(out);
        out.writeDouble(value);
    }

    @Override
    public void readBytes(ByteBuf in) {
        super.readBytes(in);
        value = in.readDouble();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof DoublePacket)) return false;
        DoublePacket packet = (DoublePacket) obj;
        return packet.value == value;
    }

    @Override
    public String toString() {
        return "DoublePacket{" +
                "value=" + value +
                '}';
    }
}
