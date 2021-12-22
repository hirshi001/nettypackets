package nettypackets.restapi;

import io.netty.util.concurrent.*;


public class RestFuture<T> extends DefaultPromise<T> {

    private final RestAction<T> restAction;

    public RestFuture(RestAction<T> restAction, EventExecutor executor) {
        super(executor);
        this.restAction = restAction;
    }

    public RestAction<T> getRestAction(){
        return restAction;
    }

    public RestFuture<T> perform(){
        return restAction.perform();
    }

    @Override
    public RestFuture<T> setSuccess(T result) {
        super.setSuccess(result);
        return this;
    }


    @Override
    public RestFuture<T> setFailure(Throwable cause) {
        super.setFailure(cause);
        return this;
    }


    @Override
    public RestFuture<T> addListener(GenericFutureListener<? extends Future<? super T>> listener) {
        super.addListener(listener);
        return this;
    }

    @Override
    public RestFuture<T> addListeners(GenericFutureListener<? extends Future<? super T>>... listeners) {
        super.addListeners(listeners);
        return this;
    }

    @Override
    public RestFuture<T> removeListener(GenericFutureListener<? extends Future<? super T>> listener) {
        super.removeListener(listener);
        return this;
    }

    @Override
    public RestFuture<T> removeListeners(GenericFutureListener<? extends Future<? super T>>... listeners) {
        super.removeListeners(listeners);
        return this;
    }

    @Override
    public RestFuture<T> await() throws InterruptedException {
        super.await();
        return this;
    }

    @Override
    public RestFuture<T> awaitUninterruptibly() {
        super.awaitUninterruptibly();
        return this;
    }


    @Override
    public RestFuture<T> sync() throws InterruptedException {
        super.sync();
        return this;
    }

    @Override
    public RestFuture<T> syncUninterruptibly() {
        super.syncUninterruptibly();
        return this;
    }
}
