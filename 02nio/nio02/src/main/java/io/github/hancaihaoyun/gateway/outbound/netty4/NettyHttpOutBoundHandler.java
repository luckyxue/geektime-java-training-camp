package io.github.hancaihaoyun.gateway.outbound.netty4;

import io.github.hancaihaoyun.gateway.outbound.IHttpOutBoundHandler;
import io.github.hancaihaoyun.gateway.router.HttpEndPointRouterImpl;
import io.github.hancaihaoyun.gateway.router.IHttpEndpointRouter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NettyHttpOutBoundHandler implements IHttpOutBoundHandler {

    private String backendUrl;
    private List<String> backendURls;
    private IHttpEndpointRouter iHttpEndpointRouter;

    public NettyHttpOutBoundHandler(String backendUrl) {
        this.backendUrl = backendUrl.endsWith("/") ? backendUrl.substring(0, backendUrl.length() - 1) : backendUrl;
        this.backendURls = new ArrayList<>();
        this.backendURls.add("http://localhost:8088/api/hello");
        this.backendURls.add("http://localhost:8088/api/hello");
        this.backendURls.add("http://localhost:8088/api/hello");

        this.iHttpEndpointRouter = new HttpEndPointRouterImpl();
    }

    @Override
    public void handle(final FullHttpRequest fullHttpRequest, final ChannelHandlerContext ctx) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        // 随机路由
        final String backendUrl = iHttpEndpointRouter.route(this.backendURls);
        try {
            Bootstrap b = new Bootstrap();
            // 客户端不需要设置这些参数？!
//            b.option(ChannelOption.SO_BACKLOG, 128);
//            b.option(ChannelOption.SO_KEEPALIVE, true);
//            b.option(ChannelOption.TCP_NODELAY, true);
//            b.option(ChannelOption.SO_SNDBUF, 32 * 1024);
//            b.option(ChannelOption.SO_RCVBUF, 32 * 1024);

            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel socketChannel) throws Exception {
                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                    socketChannel.pipeline().addLast(new HttpResponseDecoder());
                    // 客户端发送的是httpRequest，所以要使用HttpRequestEncoder进行编码
                    socketChannel.pipeline().addLast(new HttpRequestEncoder());
                    socketChannel.pipeline().addLast(new HttpObjectAggregator(1024 * 1024));
                    socketChannel.pipeline().addLast(new NettyHttpClientOutboundHandler(fullHttpRequest, ctx, backendUrl));
                }
            });
            URL url = new URL(backendUrl);
            int port = url.getPort() > 0 ? url.getPort() : 80;
            String host = url.getHost();
            ChannelFuture f = b.connect(host, port).sync();

            f.channel().write(fullHttpRequest);
            f.channel().flush();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}