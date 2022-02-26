package nettypackets.util.defaultpackets.objectpackets;

import io.netty.buffer.ByteBuf;
import nettypackets.packet.Packet;
import nettypackets.util.ByteBufSerializable;

import java.util.function.Supplier;

/**
 * Any class which overrides {@link ByteBufSerializable} should implement the
 * {@link ByteBufSerializableObjectPacket#supply()} method to create a new object each time it is called (similar to a
 * factory method).
 * <br>
 * For example
 * <pre>
 * {@code
 *  @Override
 *  public MyObject get() {
 *      return new MyObject();
 *  }
 * }
 * </pre>
 * or
 * <pre>
 *  {@code
 *  static Supplier<MyObject> myObjectSupplier = () -> new MyObject(); // or MyObject::new or any other supplier
 *
 *  @Override
 *  public MyObject get() {
 *      return myObjectSupplier.get();
 *  }
 *  }
 *  </pre>
 * @param <T>
 */
public abstract class ByteBufSerializableObjectPacket<T extends ByteBufSerializable> extends Packet {

    private T object;

    public ByteBufSerializableObjectPacket(T object){
        this.object = object;
    }

    public ByteBufSerializableObjectPacket(){

    }

    @Override
    public final void writeBytes(ByteBuf out) {
        super.writeBytes(out);
        object.writeBytes(out);
    }

    @Override
    public final void readBytes(ByteBuf in) {
        super.readBytes(in);
        object = supply();
        object.readBytes(in);
    }

    public final T getObject() {
        return object;
    }

    protected abstract T supply();

    public final void set(T object){
        this.object = object;
    }
}
