package com.kruk.gateway.security;

import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class RouterValidator {

    public static final List<Pattern> openEndpoints = List.of(
            Pattern.compile("/auth/user/signup"),
            Pattern.compile("/auth/token/generate"),
            Pattern.compile("/auth/v3/api-docs.*"),
            Pattern.compile("/api/v3/api-docs.*"),
            Pattern.compile("/payment/v3/api-docs.*"),
            Pattern.compile("/inventory/v3/api-docs.*"),
            Pattern.compile("/delivery/v3/api-docs.*"),
            Pattern.compile("/api/actuator/prometheus"),
            Pattern.compile("/payment/actuator/prometheus"),
            Pattern.compile("/inventory/actuator/prometheus"),
            Pattern.compile("/delivery/actuator/prometheus")

    );

    public static final Predicate<ServerHttpRequest> isSecured =
            request -> openEndpoints
                    .stream()
                    .noneMatch(pattern -> pattern.matcher(request.getURI().getPath()).matches());

}
