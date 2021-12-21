package nettypackets.packet;

import io.netty.channel.ChannelHandlerContext;

public interface PacketHandler<T extends Packet> {

    static final PacketHandler<?> NO_HANDLE = new PacketHandler<Packet>() {
        @Override
        public void handle(Packet packet) {
            // Do nothing
        }
    };

    @SuppressWarnings("unchecked")
    public static <A extends Packet> PacketHandler<A> noHandle(){
        return (PacketHandler<A>) NO_HANDLE;
    }

    public void handle(T packet);

}
