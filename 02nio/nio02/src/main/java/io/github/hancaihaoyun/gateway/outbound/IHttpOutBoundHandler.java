package io.github.hancaihaoyun.gateway.outbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface IHttpOutBoundHandler {

    void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) throws Exception;
}
