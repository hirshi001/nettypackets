package nettypackets.util.defaultpackets.primitivepackets;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;

public class BytePacket extends Packet {

    public byte data;

    public BytePacket(byte value) {
        this.data = value;
    }

    @Override
    public void writeBytes(ByteBuf out) {
        super.writeBytes(out);
        out.writeByte(data);
    }

    @Override
    public void readBytes(ByteBuf in) {
        super.readBytes(in);
        data = in.readByte();
    }

    public BytePacket(){

    }

    public byte getValue() {
        return data;
    }

    public void setValue(byte value) {
        this.data = value;
    }

}
