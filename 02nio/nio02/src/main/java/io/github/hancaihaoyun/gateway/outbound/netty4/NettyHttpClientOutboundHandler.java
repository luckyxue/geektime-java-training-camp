package io.github.hancaihaoyun.gateway.outbound.netty4;

import io.github.hancaihaoyun.gateway.inbound.HttpInboundHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class NettyHttpClientOutboundHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);

    private ByteBufToBytes reader;
    private ChannelHandlerContext parentCtx;
    private FullHttpRequest fullHttpRequest = null;
    private String backendUrl;

    public NettyHttpClientOutboundHandler(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx, String backendUrl) {
        this.fullHttpRequest = fullHttpRequest;
        this.parentCtx = ctx;
        this.backendUrl = backendUrl;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String host = backendUrl.replaceAll("/", "").split(":")[1];
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, fullHttpRequest.method(), new URI(backendUrl).toASCIIString());
        // 构建http请求
        request.headers().set(HttpHeaderNames.HOST, host);
        request.headers().set(HttpHeaderNames.CONNECTION,
                HttpHeaderNames.CONNECTION);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH,
                request.content().readableBytes());
        ctx.writeAndFlush(request);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        int contentLength = 0;
        logger.info("=========");
        for (Map.Entry<String, String> e : this.fullHttpRequest.headers()) {
            logger.info(e.getKey() + " => " + e.getValue());
        }
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            if (HttpUtil.isContentLengthSet(response)) {
                contentLength = (int) HttpUtil.getContentLength(response);
                reader = new ByteBufToBytes(contentLength);
            }
        }
        if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            reader.reading(content);
            content.release();
            byte[] bytes = reader.readFull();
            logger.info("=========");
            logger.info(new String(bytes));
            logger.info(String.valueOf(bytes.length));
            if (reader.isEnd()) {
                FullHttpResponse response = null;
                try {
                    response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(bytes));
                    response.headers().set("Content-Type", "text/html;charset=UTF-8");
                    response.headers().setInt("Content-Length", contentLength);
                    logger.info("=========");
                    for (Map.Entry<String, String> e : response.headers()) {
                        logger.info(e.getKey() + " => " + e.getValue());
                    }
                } catch (Exception e) {
                    response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
                    exceptionCaught(parentCtx, e);
                } finally {
                    parentCtx.write(response);
                }
                parentCtx.flush();
                ctx.close();
            }
        }
    }
}