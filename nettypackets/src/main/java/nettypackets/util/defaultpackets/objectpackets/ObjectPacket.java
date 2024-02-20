package nettypackets.util.defaultpackets.objectpackets;

import io.github.pixee.security.ObjectInputFilters;
import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;

import java.io.*;

public class ObjectPacket<T> extends Packet {

    private T object;
    private Throwable cause;
    private boolean failed;

    public ObjectPacket(T object) {
        this.object = object;
    }

    public ObjectPacket() {
        super();
    }

    @Override
    public void writeBytes(ByteBuf out) {
        super.writeBytes(out);
        try {
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(byteOutStream);
            outputStream.writeObject(object);
            outputStream.flush();

            byte[] data = byteOutStream.toByteArray();
            out.writeInt(data.length);
            out.writeBytes(data);

            outputStream.close();
        } catch (IOException e) {
            cause = e;
            failed = true;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void readBytes(ByteBuf in) {
        super.readBytes(in);
        try {

            int size = in.readInt();
            byte[] data = new byte[size];
            in.readBytes(data);
            ByteArrayInputStream byteInStream = new ByteArrayInputStream(data);
            ObjectInputStream inputStream = new ObjectInputStream(byteInStream);
            ObjectInputFilters.enableObjectFilterIfUnprotected(inputStream);
            object = (T)inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            cause = e;
            failed = true;
        }
    }

    public boolean failed(){
        return failed;
    }

    public Throwable getCause(){
        return cause;
    }
}
