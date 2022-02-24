package nettypackets.network;

import io.netty.channel.Channel;
import io.netty.util.concurrent.EventExecutor;
import nettypackets.network.packethandlercontext.PacketHandlerContext;
import nettypackets.packet.Packet;
import nettypackets.packetregistry.PacketRegistry;
import nettypackets.restapi.DefaultRestAction;
import nettypackets.restapi.Operation;
import nettypackets.restapi.Operation.*;
import nettypackets.restapi.RestAction;
import nettypackets.util.tuple.Pair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketResponseManager {


    private final AtomicInteger packetResponseId;
    private final Map<Integer, Operation<PacketHandlerContext<?>, ?>> packetResponses;
    private final EventExecutor executor;

    public PacketResponseManager(EventExecutor executor) {
        super();
        packetResponseId = new AtomicInteger(0);
        packetResponses = new ConcurrentHashMap<>();
        this.executor = executor;
    }

    public RestAction<PacketHandlerContext<?>> submit(Packet packet, Runnable runnable, long timeout, TimeUnit unit) {
        SupplyOperation supplyOperation = (next, promise) -> {
            int id = getNextPacketResponseId();
            packet.sendingId = id;
            packetResponses.put(id, next);
            runnable.run();
            executor.schedule(()-> {
                Operation<PacketHandlerContext<?>, ?> operation = packetResponses.remove(id);
                if(operation!=null && !operation.promise.isSuccess()){
                    operation.promise.setFailure(new TimeoutException("Packet did not arrive. Id = " + id));
                }
            }, timeout, unit);
        };

        return new DefaultRestAction<>(supplyOperation, executor);
    }

    public void success(PacketHandlerContext<?> context){
        Operation<PacketHandlerContext<?>, ?> operation = packetResponses.remove(context.packet.receivingId);
        if(operation!=null) operation.submitTask(context);
    }

    public void noId(Packet packet){
        packet.sendingId = -1;
    }

    private int getNextPacketResponseId(){
        return packetResponseId.incrementAndGet();
    }
}
