package nettypackets.util;

import io.netty.buffer.ByteBuf;

public interface ByteBufSerializable {

    void readBytes(ByteBuf in);

    void writeBytes(ByteBuf out);

}
