package io.github.hancaihaoyun.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface IHttpRequestFilter {

    void filter(final FullHttpRequest fullRequest, ChannelHandlerContext ctx);

}
