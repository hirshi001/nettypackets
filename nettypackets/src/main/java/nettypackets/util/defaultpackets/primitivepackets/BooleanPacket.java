package nettypackets.util.defaultpackets.primitivepackets;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;

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
    public String toString() {
        return "BooleanPacket{" +
                "value=" + value +
                '}';
    }
}
