package nettypackets.iohandlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import nettypackets.network.NetworkSide;
import nettypackets.network.packethandlercontext.PacketHandlerContext;
import nettypackets.network.packethandlercontext.PacketType;

import java.util.List;

public class DatagramPacketInboundDecoder extends MessageToMessageDecoder<DatagramPacket> {

    private final NetworkSide networkSide;

    public DatagramPacketInboundDecoder(NetworkSide networkSide){
        this.networkSide = networkSide;
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        PacketHandlerContext<?> context = networkSide.getNetworkData().decode(msg.content());
        context.source = msg.sender();
        context.networkSide = networkSide;
        context.ctx = ctx;
        context.packetType = PacketType.UDP;
        out.add(context);
    }

}
