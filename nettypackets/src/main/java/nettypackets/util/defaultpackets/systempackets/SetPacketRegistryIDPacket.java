package nettypackets.util.defaultpackets.systempackets;

import io.netty.buffer.ByteBuf;
import nettypackets.network.packethandlercontext.PacketHandlerContext;
import nettypackets.packet.Packet;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.packetregistrycontainer.PacketRegistryContainer;
import nettypackets.util.ByteBufUtil;

public class SetPacketRegistryIDPacket extends Packet {

    public String registryName;
    public int registryId;

    @Override
    public void writeBytes(ByteBuf out) {
        super.writeBytes(out);
        ByteBufUtil.writeStringToBuf(registryName, out);
        out.writeInt(registryId);
    }

    @Override
    public void readBytes(ByteBuf in) {
        super.readBytes(in);
        registryName = ByteBufUtil.readStringFromBuf(in);
        registryId = in.readInt();
    }

    public static void clientHandle(PacketHandlerContext<SetPacketRegistryIDPacket> context) {
        PacketRegistryContainer container = context.networkData.getPacketRegistryContainer();
        PacketRegistry registry = container.get(context.packet.registryName);
        if(registry!=null) {
            container.setPacketRegistryID(registry, context.packet.registryId);
        }
    }
}
