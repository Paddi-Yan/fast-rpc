package com.paddi.core.server;

import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Author: Paddi-Yan
 * @Project: irpc-framework
 * @CreatedTime: 2023年02月08日 21:20:59
 */
@Slf4j
@ChannelHandler.Sharable
public class MaxConnectionLimitHandler extends ChannelInboundHandlerAdapter {
    private final int MAX_CONNECTION_LIMIT;
    private final AtomicInteger currentConnectionCount = new AtomicInteger(0);
    private final LongAdder droppedConnectionCount = new LongAdder();
    private final AtomicBoolean loggingScheduled = new AtomicBoolean(false);

    public MaxConnectionLimitHandler(int maxConnection) {
        this.MAX_CONNECTION_LIMIT = maxConnection;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = (Channel) msg;
        int connection = currentConnectionCount.incrementAndGet();
        if(connection > 0 && connection <= MAX_CONNECTION_LIMIT) {
            channel.closeFuture().addListener(future -> currentConnectionCount.decrementAndGet());
            super.channelRead(ctx, msg);
        }else {
            currentConnectionCount.decrementAndGet();
            //避免产生大量的TIME_WAIT连接
            channel.config().setOption(ChannelOption.SO_LINGER, 0);
            channel.unsafe().closeForcibly();
            droppedConnectionCount.increment();
            //CAS减少并发请求压力并定期执行日志打印
            if(loggingScheduled.compareAndSet(false, true)) {
                ctx.executor().schedule(this::writeDroppedConnectionLog, 5, TimeUnit.SECONDS);
            }
        }
    }

    private void writeDroppedConnectionLog() {
        loggingScheduled.set(false);
        long dropped = droppedConnectionCount.sumThenReset();
        if(dropped > 0) {
            log.error("Dropped {} connection(s) to protect server, maxConnection is {}", dropped, MAX_CONNECTION_LIMIT);
        }
    }
}
