package example;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;
import nettypackets.util.ByteBufUtil;

public class PersonPacket extends Packet {

    //You can choose to keep these variables as public
    //Or then can be private and you can add getter methods (and setter methods if necessary)
    public String name;
    public int age;

    public PersonPacket() {
        super();
    }

    public PersonPacket(String name, int age){
        this.name = name;
        this.age = age;
    }

    @Override
    public void writeBytes(ByteBuf out) {
        super.writeBytes(out);
        ByteBufUtil.writeStringToBuf(name, out);
        out.writeInt(age);
    }

    @Override
    public void readBytes(ByteBuf in) {
        super.readBytes(in);
        //make sure order is preserved, whatever is written first should be read first
        name = ByteBufUtil.readStringFromBuf(in);
        age = in.readInt();
    }

}
