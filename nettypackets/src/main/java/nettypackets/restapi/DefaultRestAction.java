package nettypackets.restapi;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import nettypackets.restapi.Operation.*;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

public class DefaultRestAction<T>  implements RestAction<T>{

    private final Operation<?, T> supplyOperation;

    final AtomicBoolean started;

    RestFuture promise;
    Operation head;
    Operation tail;

    ScheduledExecutorService executorService;
    EventExecutor executor;

    public DefaultRestAction(SupplyOperation<T> supplyOperation, ScheduledExecutorService executorService, EventExecutor executor) {
        this.supplyOperation = new HeadOperation<>(supplyOperation);
        this.executorService = executorService;
        this.executor = executor;

        tail = head = new BlankOperation<T>();
        promise = new RestFuture(this, executor);
        this.supplyOperation.promise = promise;
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
        return addNewOperation(new PauseOperation<>(timeout, executorService));
    }

    public <O> RestAction<O> addNewOperation(Operation<T, O> operation) {
        tail.next = operation;
        tail = tail.next;
        operation.promise = promise;
        return (RestAction<O>) this;
    }

    @Override
    public RestAction<T> pauseFor(long timeout, TimeUnit unit) {
        return pauseFor(unit.toMillis(timeout));
    }

    @Override
    public RestFuture<T> perform() {
        synchronized (started) {
            if(started.getAndSet(true)) return promise;

            addNewOperation(new TailOperation<>());

            supplyOperation.next = head;
            executor.submit(() -> supplyOperation.submitTask(null));

            return promise;
        }
    }

    @Override
    public RestFuture<T> getRestFuture() {
        return promise;
    }


}
