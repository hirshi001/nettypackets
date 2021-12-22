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
}
