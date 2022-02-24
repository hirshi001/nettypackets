package nettypackets.restapi;

import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;
import nettypackets.restapi.Operation.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

public class DefaultRestAction<T>  implements RestAction<T>{

    protected static final EventExecutor DEFAULT_EVENT_EXECUTOR = new DefaultEventExecutor();

    protected final EventExecutor eventExecutor;
    protected final AtomicBoolean started;

    protected RestFuture<T> promise;
    protected Operation<?, T> head;
    protected Operation tail;


    public DefaultRestAction(SupplyOperation<T> supplyOperation) {
        this(supplyOperation, DEFAULT_EVENT_EXECUTOR);
    }

    public DefaultRestAction(SupplyOperation<T> supplyOperation, EventExecutor eventExecutor) {
        this.eventExecutor = eventExecutor;

        promise = new RestFuture<>(this, eventExecutor);

        head = tail = new HeadOperation<>(supplyOperation);
        head.promise = this.promise;

        started = new AtomicBoolean(false);
    }

    @Override
    public RestAction<T> then(Consumer<T> consumer) {
        return addNewOperation(new ThenOperation<>(consumer));
    }

    @Override
    public <O> RestAction<O> map(Function<T, O> function) {
        return addNewOperation(new MapOperation<>(function));
    }

    @Override
    public RestAction<T> pauseFor(long timeout) {
        return pauseFor(timeout, TimeUnit.MILLISECONDS);
    }

    public <O> RestAction<O> addNewOperation(Operation<T, O> operation) {
        tail.next = operation;
        tail = tail.next;
        operation.promise = (RestFuture<O>) promise;
        return (RestAction<O>) this;
    }

    @Override
    public RestAction<T> pauseFor(long timeout, TimeUnit unit) {
        return addNewOperation(new PauseOperation<>(timeout, unit, eventExecutor));
    }

    @Override
    public RestFuture<T> perform() {
        if(started.getAndSet(true)) return promise;
        addNewOperation(new TailOperation<>());
        eventExecutor.submit(() -> head.submitTask(null));
        return promise;
    }

    @Override
    public RestFuture<T> getRestFuture() {
        return promise;
    }


}
