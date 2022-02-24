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
    public String toString() {
        return "CharPacket{" +
                "value=" + value +
                '}';
    }
}
