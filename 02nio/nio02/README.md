# netty-gateway

```
1. 实现一个简单的包含过滤和路由转发功能的网关
2. 分别使用Apache Async Http、Netty和OkHttp实现转发请求的的出站响应功能

// Apache Async Http
handler = new HttpOutboundHandler(this.proxyServer);
// Netty Http
handler = new NettyHttpOutBoundHandler(this.proxyServer);
// Ok Http
handler = new OkhttpOutboundHandler(this.proxyServer);

wrk -c 40 -d30s http://localhost:8888/api/hello
Running 30s test @ http://localhost:8888/api/hello
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    31.75ms   55.69ms 674.72ms   97.92%
    Req/Sec   814.98     83.18     1.01k    86.01%
  47849 requests in 30.10s, 3.74MB read
Requests/sec:   1589.45
Transfer/sec:    127.28KB

wrk -c 40 -d30s http://localhost:8888/api/hello
Running 30s test @ http://localhost:8888/api/hello
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.09s   447.15ms   1.99s    64.52%
    Req/Sec    19.81     15.38   131.00     75.24%
  1008 requests in 30.10s, 87.61KB read
  Socket errors: connect 0, read 0, write 0, timeout 71
Requests/sec:     33.49
Transfer/sec:      2.91KB

wrk -c 40 -d30s http://localhost:8888/api/hello
Running 30s test @ http://localhost:8888/api/hello
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    63.16ms   39.42ms 532.63ms   98.00%
    Req/Sec   336.26     33.55   425.00     82.51%
  19877 requests in 30.10s, 2.01MB read
Requests/sec:    660.39
Transfer/sec:     68.36KB
```