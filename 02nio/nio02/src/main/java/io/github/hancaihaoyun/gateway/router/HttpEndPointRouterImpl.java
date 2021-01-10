package io.github.hancaihaoyun.gateway.router;

import java.util.List;
import java.util.Random;

public class HttpEndPointRouterImpl implements IHttpEndpointRouter {

    private final Random random;

    public HttpEndPointRouterImpl() {
        this.random = new Random();
    }

    @Override
    public String route(List<String> endpoints) {
        int num = random.nextInt(endpoints.size());
        return endpoints.get(num);
    }
}
