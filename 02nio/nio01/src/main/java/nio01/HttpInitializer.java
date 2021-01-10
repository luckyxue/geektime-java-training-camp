package nio01;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.ssl.SslContext;

public class HttpInitializer extends ChannelInitializer<SocketChannel> {
    private SslContext sslCtx;

    public HttpInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline channelPipeline = channel.pipeline();
        if (sslCtx != null) {
            channelPipeline.addLast(sslCtx.newHandler(channel.alloc()));
        }
        channelPipeline.addLast(new HttpServerCodec());
//        channelPipeline.addLast(new HttpServerExpectContinueHandler());
        channelPipeline.addLast(new HttpObjectAggregator(1024 * 1024));
        channelPipeline.addLast(new HttpHandler());
    }
}
