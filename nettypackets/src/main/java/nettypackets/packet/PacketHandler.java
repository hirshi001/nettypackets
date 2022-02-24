package nettypackets.packet;

import io.netty.channel.ChannelHandlerContext;
import nettypackets.network.packethandlercontext.PacketHandlerContext;

public interface PacketHandler<T extends Packet> {

    public static final PacketHandler<?> NO_HANDLE = new PacketHandler<Packet>() {
        @Override
        public void handle(PacketHandlerContext<Packet> context) {

        }
    };

    @SuppressWarnings("unchecked")
    public static <A extends Packet> PacketHandler<A> noHandle(){
        return (PacketHandler<A>) NO_HANDLE;
    }

    public void handle(PacketHandlerContext<T> context);

}
