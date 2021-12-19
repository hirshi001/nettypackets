package nettypackets.network;

import io.netty.util.concurrent.AbstractFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import nettypackets.packet.Packet;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PacketResponseFuture extends AbstractFuture<Packet> {

    private Packet response;
    private long packetResponseId;


    public PacketResponseFuture() {
        super();
    }

    @Override
    public Packet get() throws InterruptedException, ExecutionException {
        return super.get();
    }

    @Override
    public Packet get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return super.get(timeout, unit);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public boolean isCancellable() {
        return false;
    }

    @Override
    public Throwable cause() {
        return null;
    }

    @Override
    public Future<Packet> addListener(GenericFutureListener<? extends Future<? super Packet>> listener) {
        return null;
    }

    @Override
    public Future<Packet> addListeners(GenericFutureListener<? extends Future<? super Packet>>... listeners) {
        return null;
    }

    @Override
    public Future<Packet> removeListener(GenericFutureListener<? extends Future<? super Packet>> listener) {
        return null;
    }

    @Override
    public Future<Packet> removeListeners(GenericFutureListener<? extends Future<? super Packet>>... listeners) {
        return null;
    }

    @Override
    public Future<Packet> sync() throws InterruptedException {
        return await();
    }

    @Override
    public Future<Packet> syncUninterruptibly() {
        return awaitUninterruptibly();
    }

    @Override
    public Future<Packet> await() throws InterruptedException {
        await(Long.MAX_VALUE);
        return this;
    }

    @Override
    public Future<Packet> awaitUninterruptibly() {
        awaitUninterruptibly(Long.MAX_VALUE);
        return this;
    }

    @Override
    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return awaitUninterruptibly(unit.toMillis(timeout));
    }

    @Override
    public boolean await(long timeoutMillis) throws InterruptedException {


        return false;
    }

    @Override
    public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
        return awaitUninterruptibly(unit.toMillis(timeout));
    }

    @Override
    public boolean awaitUninterruptibly(long timeoutMillis) {
        return false;
    }

    @Override
    public Packet getNow() {
        return null;
    }

    //not cancellable
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    //not cancellable
    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }
}
