package io.github.hancaihaoyun.gateway.inbound;

import io.github.hancaihaoyun.gateway.filter.HttpMethodRequestFilterImpl;
import io.github.hancaihaoyun.gateway.filter.IHttpRequestFilter;
import io.github.hancaihaoyun.gateway.filter.UriHttpRequestFilterImpl;
import io.github.hancaihaoyun.gateway.outbound.IHttpOutBoundHandler;
import io.github.hancaihaoyun.gateway.outbound.httpclient4.HttpOutboundHandler;
import io.github.hancaihaoyun.gateway.outbound.netty4.NettyHttpOutBoundHandler;
import io.github.hancaihaoyun.gateway.outbound.okhttp.OkhttpOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);

    private final String proxyServer;
    private IHttpOutBoundHandler handler;

    public HttpInboundHandler(String proxyServer) {
        this.proxyServer = proxyServer;
//        // Apache Async Http
//        handler = new HttpOutboundHandler(this.proxyServer);
//        // Netty Http
//        handler = new NettyHttpOutBoundHandler(this.proxyServer);
        // Ok Http
        handler = new OkhttpOutboundHandler(this.proxyServer);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
//            String uri = fullRequest.uri();
//            //logger.info("接收到的请求url为{}", uri);
//            if (uri.contains("/test")) {
//                handlerTest(fullRequest, ctx);
//            }
            IHttpRequestFilter methodHttpRequestFilter = new HttpMethodRequestFilterImpl();
            methodHttpRequestFilter.filter(fullRequest, ctx);
            IHttpRequestFilter uriHttpRequestFilter = new UriHttpRequestFilterImpl();
            uriHttpRequestFilter.filter(fullRequest, ctx);
            handler.handle(fullRequest, ctx);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

//    private void handlerTest(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
//        FullHttpResponse response = null;
//        try {
//            String value = "hello,nio";
//            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes("UTF-8")));
//            response.headers().set("Content-Type", "application/json");
//            response.headers().setInt("Content-Length", response.content().readableBytes());
//
//        } catch (Exception e) {
//            logger.error("处理测试接口出错", e);
//            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
//        } finally {
//            if (fullRequest != null) {
//                if (!HttpUtil.isKeepAlive(fullRequest)) {
//                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
//                } else {
//                    response.headers().set(CONNECTION, KEEP_ALIVE);
//                    ctx.write(response);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
//        ctx.close();
//    }

}
