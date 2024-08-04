package com.licence.service.client;

import com.licence.model.Organization;
import com.licence.utils.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class OrganizationDiscoveryClient {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationDiscoveryClient.class);

    private final DiscoveryClient discoveryClient;

    public OrganizationDiscoveryClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public Organization getOrganization(String organizationId) {

        logger.info("get organization by using discovery client");

        RestTemplate restTemplate = new RestTemplate();
        List<ServiceInstance> instances = discoveryClient.getInstances("organization-server");

        if(instances.isEmpty()) {
            return null;
        }
        String serverUri = String.format("%s/v1/organization/%s", instances.get(0).getUri(), organizationId);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.AUTHORIZATION, UserContext.getAuthToken());
        RequestEntity<String> request = new RequestEntity<>(headers ,HttpMethod.GET, null);

        ResponseEntity<Organization> restExchange = restTemplate.exchange(serverUri, HttpMethod.GET,
                request, Organization.class, organizationId);
        return restExchange.getBody();
    }
}
