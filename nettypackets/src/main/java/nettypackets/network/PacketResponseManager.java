package nettypackets.network;

import io.netty.channel.Channel;
import io.netty.util.concurrent.EventExecutor;
import nettypackets.packet.Packet;
import nettypackets.restapi.DefaultRestAction;
import nettypackets.restapi.Operation;
import nettypackets.restapi.Operation.*;
import nettypackets.restapi.RestAction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class PacketResponseManager {


    private final AtomicInteger packetResponseId;
    private final Map<Integer, Operation<Packet, ?>> packetResponses;
    private ScheduledExecutorService executorService;
    private EventExecutor executor;

    public PacketResponseManager(EventExecutor executor, ScheduledExecutorService executorService) {
        super();
        packetResponseId = new AtomicInteger(0);
        packetResponses = new ConcurrentHashMap<>();
        this.executor = executor;
        this.executorService = executorService;
    }

    public RestAction<Packet> submit(Packet packet, Channel channel, long timeout, TimeUnit unit) {

        SupplyOperation supplyOperation = (next, promise) -> {
            int id = getNextPacketResponseId();
            packet.sendingId = id;
            packetResponses.put(id, next);
            channel.writeAndFlush(packet);

            executorService.schedule(()-> {
                Operation<Packet, ?> operation = packetResponses.remove(id);
                if(operation!=null) operation.promise.setFailure(new TimeoutException("Packet did not arrive"));
            }, timeout, unit);
        };

        return new DefaultRestAction<>(supplyOperation, executorService, executor);
    }

    public void success(Packet packet){
        Operation<Packet, ?> operation = packetResponses.remove(packet.receivingId);
        if(operation!=null) operation.submitTask(packet);
    }

    public void noId(Packet packet){
        packet.sendingId = -1;
    }

    private int getNextPacketResponseId(){
        return packetResponseId.incrementAndGet();
    }
}
