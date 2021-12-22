package nettypackets.restapi;

import io.netty.util.concurrent.Promise;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Operation<A, B>{
    public Operation<B, ?> next;
    public Promise<Object> promise;

    public void submitTask(A input){
    }


    public static interface SupplyOperation<T>{
        void submitTask(Operation<T, ?> next, Promise promise);
    }

    static class HeadOperation<A> extends Operation<Object, A>{
        SupplyOperation supplyOperation;

        public HeadOperation(SupplyOperation supplyOperation){
            this.supplyOperation = supplyOperation;
        }

        @Override
        public void submitTask(Object input) {
            supplyOperation.submitTask(next, promise);
        }
    }

    static class ThenOperation<A> extends Operation<A, A>{

        Consumer<A> consumer;

        public ThenOperation(Consumer<A> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void submitTask(A input) {
            consumer.accept(input);
            next.submitTask(input);
        }

    }

    static class MapOperation<A, B> extends Operation<A, B>{

        Function<A, B> function;
        public MapOperation(Function<A, B> function) {
            this.function = function;
        }

        @Override
        public void submitTask(A input) {
            next.submitTask(function.apply(input));
        }
    }

    static class PauseOperation<A> extends Operation<A, A>{

        long timeout;
        ScheduledExecutorService executorService;
        public PauseOperation(long timeout, ScheduledExecutorService executorService) {
            this.timeout = timeout;
            this.executorService = executorService;
        }

        @Override
        public void submitTask(A input) {
            executorService.schedule(() -> next.submitTask(input), timeout, TimeUnit.MILLISECONDS);
        }

    }

    /**
     *
     * @param <A>
     */
    static class TailOperation<A> extends Operation<A, Void>{

        @Override
        public void submitTask(A input) {
            promise.setSuccess(input); //dont call next, it should be the last operation
        }

    }

    static class BlankOperation<A> extends Operation<A, A>{
        @Override
        public void submitTask(A input) {
            next.submitTask(input);
        }
    }

}