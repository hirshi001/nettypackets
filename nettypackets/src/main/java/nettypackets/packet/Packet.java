package nettypackets.packet;

import io.netty.buffer.ByteBuf;

//Test Comment
public abstract class Packet {

    public Packet(){}

    public void writeBytes(ByteBuf out){
        System.out.println("test #3");
    }

    public void readBytes(ByteBuf in){  }

}
