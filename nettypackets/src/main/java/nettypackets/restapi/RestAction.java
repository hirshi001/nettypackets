package nettypackets.restapi;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

public interface RestAction<T> {

    public RestAction<T> then(Consumer<T> consumer);

    public <O> RestAction<O> map(Function<T, O> function);

    public RestAction<T> pauseFor(long timeout);

    public RestAction<T> pauseFor(long timeout, TimeUnit unit);

    public RestFuture<T> perform();

    public RestFuture<T> getRestFuture();




}
