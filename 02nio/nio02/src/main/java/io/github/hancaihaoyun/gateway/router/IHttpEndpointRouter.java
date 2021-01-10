package io.github.hancaihaoyun.gateway.router;

import java.util.List;

public interface IHttpEndpointRouter {

    String route(List<String> endpoints);

    // Load Balance
    // Random
    // RoundRibbon 
    // Weight
    // - server01,20
    // - server02,30
    // - server03,50

}
