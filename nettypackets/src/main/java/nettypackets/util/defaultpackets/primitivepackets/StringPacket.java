package nettypackets.util.defaultpackets.primitivepackets;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;
import nettypackets.util.ByteBufUtil;

import java.util.Objects;

public class StringPacket extends Packet {

    public String value;

    public StringPacket(){

    }

    public StringPacket(String value){
        this.value = value;
    }

    @Override
    public void writeBytes(ByteBuf out) {
        super.writeBytes(out);
        ByteBufUtil.writeStringToBuf(value, out);
    }

    @Override
    public void readBytes(ByteBuf in) {
        super.readBytes(in);
        value = ByteBufUtil.readStringFromBuf(in);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof StringPacket)) return false;
        StringPacket packet = (StringPacket) obj;
        return Objects.equals(packet.value, value);
    }

    @Override
    public String toString() {
        return "StringPacket{" +
                "value='" + value + '\'' +
                '}';
    }
}
