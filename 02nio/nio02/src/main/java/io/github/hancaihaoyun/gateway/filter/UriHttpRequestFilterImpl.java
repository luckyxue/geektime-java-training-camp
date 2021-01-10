package io.github.hancaihaoyun.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;

import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class UriHttpRequestFilterImpl implements IHttpRequestFilter {
    @Override
    public void filter(final FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        String uri = fullRequest.uri();
        System.out.println("filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx)接收到的请求,url: " + uri);
        if (!uri.contains("api/hello")) {
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
            ctx.write(response);
            ctx.flush();
            ctx.close();
        }
        HttpHeaders headers = fullRequest.headers();
        if (headers == null) {
            headers = new DefaultHttpHeaders();
        }
        headers.add("proxy-tag", this.getClass().getSimpleName());
    }
}
