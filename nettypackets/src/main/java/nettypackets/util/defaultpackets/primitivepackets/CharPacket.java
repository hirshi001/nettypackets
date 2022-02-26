package nettypackets.util.defaultpackets.primitivepackets;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;

public class CharPacket extends Packet {

    public char value;

    public CharPacket() {
        super();
    }

    public CharPacket(char value) {

    }

    @Override
    public void writeBytes(ByteBuf out) {
        super.writeBytes(out);
        out.writeChar(value);
    }

    @Override
    public void readBytes(ByteBuf in) {
        super.readBytes(in);
        value = in.readChar();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof CharPacket)) return false;
        CharPacket packet = (CharPacket) obj;
        return packet.value == value;
    }


    @Override
    public String toString() {
        return "CharPacket{" +
                "value=" + value +
                '}';
    }
}
