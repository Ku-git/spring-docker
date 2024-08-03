package com.licence.config;

import brave.Tracer;
import com.licence.utils.UserContextInterceptor;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.brave.bridge.BraveTraceContext;
import io.micrometer.tracing.propagation.Propagator;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestTemplateConfig {

    private final Tracer tracer;

    private final Propagator propagator;

    public RestTemplateConfig(Tracer tracer, Propagator propagator) {
        this.tracer = tracer;
        this.propagator = propagator;
    }

    @LoadBalanced
    @Bean(name = "RestTemplateLB")
    public RestTemplate getLoadBalanceRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        interceptors.add(new UserContextInterceptor());
        //新增micrometer的追蹤和傳播屬性到rest template
        interceptors.add((request, body, execution) -> {
            Propagator.Setter<HttpHeaders> setter = HttpHeaders::set;
            TraceContext traceContext = BraveTraceContext.fromBrave(tracer.currentSpan().context());
            propagator.inject(traceContext, request.getHeaders(), setter);
            return execution.execute(request, body);
        });

        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }


}
