package fakegram.adapter.http.prometheus;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import io.prometheus.client.Counter;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import javax.inject.Inject;

import static java.util.Objects.nonNull;

@Filter(value="/**")
public class HttpMetricsFilter implements HttpServerFilter {

    private final Counter requestCounter;
    private final Counter responseCounter;
    private final Counter trafficCounter;

    private static String SERVICE_NAME = "Account service";

    @Inject
    public HttpMetricsFilter(PrometheusMeterRegistry prometheusMeterRegistry) {
        requestCounter = Counter
                .build()
                .name("http_request_count")
                .help("Incoming request counter")
                .labelNames("service", "serverName", "port", "endpoint",  "originIp", "browser")
                .register(prometheusMeterRegistry.getPrometheusRegistry());
        responseCounter = Counter
                .build()
                .name("http_response_count")
                .help("Incoming request counter")
                .labelNames("service", "status", "endpoint")
                .register(prometheusMeterRegistry.getPrometheusRegistry());
        trafficCounter = Counter
                .build()
                .name("http_traffic_counter")
                .help("Traffic counter in bytes for services")
                .labelNames("service", "serverName", "port", "traffic")
                .register(prometheusMeterRegistry.getPrometheusRegistry());
    }

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        String userAgent = nonNull(request.getHeaders().get("User-Agent")) ? request.getHeaders().get("User-Agent") : "Unknown";

        String url = request.getPath();
        requestCounter.labels(
                SERVICE_NAME,
                request.getServerName(),
                Integer.toString(request.getServerAddress().getPort()),
                url,
                request.getRemoteAddress().getAddress().toString(),
                userAgent
            ).inc();

        long requestContentSize = request.getContentLength() < 0 ? 0 : request.getContentLength();
        trafficCounter.labels(
                SERVICE_NAME,
                request.getServerName(),
                Integer.toString(request.getServerAddress().getPort()),
                url
            ).inc(requestContentSize);

        return Flowable.fromPublisher(chain.proceed(request))
                .doOnNext(response -> {
                    responseCounter.labels(
                            SERVICE_NAME,
                            Integer.toString(response.getStatus().getCode()),
                            url
                    ).inc();

                    long responseContentSize = response.getContentLength() < 0 ? 0 : response.getContentLength();
                    trafficCounter.labels(
                        SERVICE_NAME,
                        request.getServerName(),
                        Integer.toString(request.getServerAddress().getPort()),
                        url
                    ).inc(responseContentSize);
                });

    }

}
