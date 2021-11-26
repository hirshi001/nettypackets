package nettypackets.packet;

import io.netty.channel.ChannelHandlerContext;
import nettypackets.PacketHandlerContext;
import nettypackets.packetregistry.PacketRegistry;

public interface PacketHandler<T extends Packet> {

    static final PacketHandler NO_HANDLE = new PacketHandler() {
        @Override
        public void handle(Packet packet, PacketHandlerContext ctx) {
            // Do nothing
        }
    };

    @SuppressWarnings("unchecked")
    public static <A extends Packet> PacketHandler<A> noHandle(){
        return (PacketHandler<A>) NO_HANDLE;
    }

    public void handle(T packet, PacketHandlerContext ctx);

}
