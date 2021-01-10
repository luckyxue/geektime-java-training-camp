package io.github.hancaihaoyun.gateway.outbound.okhttp;

import io.github.hancaihaoyun.gateway.inbound.HttpInboundHandler;
import io.github.hancaihaoyun.gateway.outbound.IHttpOutBoundHandler;
import io.github.hancaihaoyun.gateway.router.HttpEndPointRouterImpl;
import io.github.hancaihaoyun.gateway.router.IHttpEndpointRouter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class OkhttpOutboundHandler extends ChannelInboundHandlerAdapter implements IHttpOutBoundHandler {

    // 缓存客户端实例
    public static OkHttpClient client = new OkHttpClient.Builder()
            .callTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .build();
    private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);
    private String backendUrl;
    private List<String> backendURls;
    private IHttpEndpointRouter iHttpEndpointRouter;

    public OkhttpOutboundHandler(String backendUrl) {
        this.backendUrl = backendUrl.endsWith("/") ? backendUrl.substring(0, backendUrl.length() - 1) : backendUrl;
        this.backendURls = new ArrayList<>();
        this.backendURls.add("http://localhost:8088/api/hello");
        this.backendURls.add("http://localhost:8088/api/hello");
        this.backendURls.add("http://localhost:8088/api/hello");

        this.iHttpEndpointRouter = new HttpEndPointRouterImpl();
    }

    @Override
    public void handle(final FullHttpRequest fullHttpRequest, final ChannelHandlerContext ctx) throws Exception {
        // 随机路由
        final String backendUrl = iHttpEndpointRouter.route(this.backendURls);
        Request request = new Request.Builder()
                .url(backendUrl)
                .build();
        try (Response response = client.newCall(request).execute()) {
            handleResponse(fullHttpRequest, ctx, response);
        }
    }

    private void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final Response endpointResponse) throws Exception {
        FullHttpResponse response = null;
        logger.info("=======");
        try {
            for (Map.Entry<String, String> e : fullRequest.headers()) {
                logger.info(e.getKey() + " => " + e.getValue());
            }
            response = convertOkhttpResponseToNettyResponse(endpointResponse);
            logger.info("=======");
            for (Map.Entry<String, String> e : response.headers()) {
                logger.info(e.getKey() + " => " + e.getValue());
            }
        } catch (Exception e) {
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    //response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }
            ctx.flush();
        }
    }

    private FullHttpResponse convertOkhttpResponseToNettyResponse(Response okhttpResponse) {
        HttpResponseStatus httpResponseStatus = HttpResponseStatus.valueOf(okhttpResponse.code());
        HttpVersion httpVersion = HttpVersion.valueOf(okhttpResponse.protocol().toString());
        ByteBuf content = null;
        try (ResponseBody body = okhttpResponse.body()) {
            content = Unpooled.wrappedBuffer(body.bytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        FullHttpResponse nettyResponse = new DefaultFullHttpResponse(httpVersion, httpResponseStatus, content);
        okhttpResponse.headers().toMultimap().forEach((key, values) -> {
            nettyResponse.headers().remove(key);
            nettyResponse.headers().add(key, String.join(",", values));
        });
        return nettyResponse;
    }

}
