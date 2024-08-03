package com.licence.service.client;

import brave.ScopedSpan;
import brave.Tracer;
import com.licence.model.Organization;
import com.licence.repository.OrganizationRedisRepository;
import com.licence.utils.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrganizationRestTemplateClient {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationRestTemplateClient.class);

    private static final String URI = "http://organizationServer/v1/organization/{organizationId}";

    private static final String GATEWAY_URI = "http://gateway-server:8072/organizationServer/v1/organization/{organizationId}";

    @Autowired
    @Qualifier("RestTemplateLB")
    private RestTemplate restTemplate;
    @Autowired
    private OrganizationRedisRepository orgRedisRepository;
    @Autowired
    private Tracer tracer;


    public Organization getOrganization(String organizationId) {

        logger.debug("In license service, get organization: {}", UserContext.getCorrelationId());

        Organization organization = checkRedisCache(organizationId);
        if (organization != null) {
            logger.debug("I have successfully retrieved organization: {} from redis cache: {}", organizationId, organization);
            return organization;
        }

        logger.debug("Unable to locate organization from the redis cache: {}", organizationId);
        ResponseEntity<Organization> restExchange = restTemplate
                .exchange(GATEWAY_URI, HttpMethod.GET, null, Organization.class, organizationId);
        organization = restExchange.getBody();
        if (organization != null) {
            cacheOrganizationToRedis(organization);
        }

        return organization;
    }

    private Organization checkRedisCache(String organizationId) {
        ScopedSpan newSpan = tracer.startScopedSpan("read License Data From Redis");
        try {
            return orgRedisRepository.findById(organizationId).orElse(null);
        } catch (Exception e) {
            logger.error("error occurred while trying to retrieve organization {}, checking redis cache.", organizationId, e);
            return null;
        } finally {
            newSpan.tag("peer.service", "redis");
            newSpan.annotate("Client received");
            newSpan.finish();
        }
    }

    private void cacheOrganizationToRedis(Organization organization) {
        try {
            orgRedisRepository.save(organization);
        } catch (Exception e) {
            logger.error("unable to cache organization {} in redis", organization.getOrganizationId(), e);
        }
    }

}
