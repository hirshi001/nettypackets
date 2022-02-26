package nettypackets.util.defaultpackets.primitivepackets;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;
import nettypackets.util.defaultpackets.arraypackets.FloatArrayPacket;

import java.util.Arrays;

public class BooleanPacket extends Packet {

    private boolean value;

    public BooleanPacket() {
        super();
    }

    public BooleanPacket(boolean value){
        this.value = value;
    }

    @Override
    public void writeBytes(ByteBuf out) {
        super.writeBytes(out);
        out.writeBoolean(value);
    }

    @Override
    public void readBytes(ByteBuf in) {
        super.readBytes(in);
        value = in.readBoolean();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof BooleanPacket)) return false;
        BooleanPacket packet = (BooleanPacket) obj;
        return packet.value == value;
    }


    @Override
    public String toString() {
        return "BooleanPacket{" +
                "value=" + value +
                '}';
    }
}
