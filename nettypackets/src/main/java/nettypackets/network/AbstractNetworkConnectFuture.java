package nettypackets.network;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.AbstractFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import nettypackets.network.client.Client;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class AbstractNetworkConnectFuture<N extends NetworkSide<B>, B extends AbstractBootstrap<B, ? extends Channel>> extends AbstractFuture<N> {

    protected N network;
    protected B bootstrap;
    protected ChannelFuture connectingFuture;
    protected volatile boolean connected;

    protected Set<GenericFutureListener<Future<? super N>>> listeners = new HashSet<>();


    public AbstractNetworkConnectFuture(N network, B boostrap) {
        this.network = network;
        this.bootstrap = boostrap;
        this.connectingFuture = network.connect(bootstrap);
        connectingFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if(future.isSuccess()) connected = true;
                AbstractNetworkConnectFuture.this.listeners.forEach(listener -> {
                    try {
                        listener.operationComplete(AbstractNetworkConnectFuture.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });

    }


    @Override
    public boolean isSuccess() {
        return connectingFuture.isSuccess();
    }

    @Override
    public boolean isCancellable() {
        return connectingFuture.isCancellable();
    }

    @Override
    public Throwable cause() {
        return connectingFuture.cause();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Future<N> addListener(GenericFutureListener<? extends Future<? super N>> listener) {
        listeners.add((GenericFutureListener<Future<? super N>>) listener);
        return this;
    }

    @Override
    public Future<N> addListeners(GenericFutureListener<? extends Future<? super N>>... listeners) {
        for(GenericFutureListener<? extends Future<? super N>> listener : listeners) {
            addListener(listener);
        }
        return this;
    }


    @Override
    @SuppressWarnings("unchecked")
    public Future<N> removeListener(GenericFutureListener<? extends Future<? super N>> listener) {
        listeners.remove((GenericFutureListener<Future<? super N>>) listener);
        return this;
    }

    @Override
    public Future<N> removeListeners(GenericFutureListener<? extends Future<? super N>>... listeners) {
        for(GenericFutureListener<? extends Future<? super N>> listener : listeners) {
            removeListener(listener);
        }
        return this;
    }

    @Override
    public Future<N> sync() throws InterruptedException {
        connectingFuture.sync();
        return this;
    }

    @Override
    public Future<N> syncUninterruptibly() {
        connectingFuture.syncUninterruptibly();
        return this;
    }

    @Override
    public Future<N> await() throws InterruptedException {
        connectingFuture.await();
        return this;
    }

    @Override
    public Future<N> awaitUninterruptibly() {
        connectingFuture.awaitUninterruptibly();
        return this;
    }

    @Override
    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return connectingFuture.await(timeout, unit);
    }

    @Override
    public boolean await(long timeoutMillis) throws InterruptedException {
        return connectingFuture.awaitUninterruptibly(timeoutMillis);
    }

    @Override
    public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
        return connectingFuture.awaitUninterruptibly(timeout, unit);
    }

    @Override
    public boolean awaitUninterruptibly(long timeoutMillis) {
        return connectingFuture.awaitUninterruptibly(timeoutMillis);
    }

    @Override
    public N getNow() {
        return network;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return connectingFuture.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return connectingFuture.isCancelled();
    }

    @Override
    public boolean isDone() {
        return connectingFuture.isDone();
    }
}
