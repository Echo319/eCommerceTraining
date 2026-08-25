package com.rdavies.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiGatewayApplicationTests {

    @Autowired
    private RouteDefinitionLocator routeDefinitionLocator;

    @Test
    void contextLoads() {
    }

    @Test
    void productCatalogDocsRouteIsRegistered() {
        StepVerifier.create(routeDefinitionLocator.getRouteDefinitions()
                .filter(routeDefinition -> "product-service-docs".equals(routeDefinition.getId()))
                .hasElements())
            .expectNext(true)
            .verifyComplete();
    }

}
