package nettypackets;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ByteBufUtil {

    public static void writeStringToBuf(Charset charset, String msg, ByteBuf buf){

        int index1 = buf.writerIndex();

        buf.ensureWritable(4);
        buf.writerIndex(index1+4);
        int size = buf.writeCharSequence(msg, charset);

        buf.writerIndex(index1);
        buf.writeInt(size);
        buf.writerIndex(index1+4+size);

    }

    public static void writeStringToBuf(String msg, ByteBuf buf){
        writeStringToBuf(StandardCharsets.UTF_8, msg, buf);
    }

    public static String readStringFromBuf(Charset charset, ByteBuf buf){
        return buf.readCharSequence(buf.readInt(), charset).toString();
    }

    public static String readStringFromBuf(ByteBuf buf){
        return readStringFromBuf(StandardCharsets.UTF_8, buf);
    }


}
