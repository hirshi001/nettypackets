package nettypackets.packet;

import io.netty.buffer.ByteBuf;

//Test Comment
public abstract class Packet {

    public Packet(){}

    public void writeBytes(ByteBuf out){    }

    public void readBytes(ByteBuf in){  }

}
