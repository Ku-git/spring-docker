package com.licence.service;

import com.licence.config.ServiceConfig;
import com.licence.model.License;
import com.licence.model.Organization;
import com.licence.repository.LicenseRepository;
import com.licence.service.client.OrganizationDiscoveryClient;
import com.licence.service.client.OrganizationFeignClient;
import com.licence.service.client.OrganizationRestTemplateClient;
import com.licence.utils.UserContextHolder;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeoutException;


@Service
@AllArgsConstructor
public class LicenseService {

    private final MessageSource messageSource;

    private final LicenseRepository licenseRepository;

    private final ServiceConfig config;

    private final OrganizationDiscoveryClient discoveryClient;

    private final OrganizationRestTemplateClient restClient;

    private final OrganizationFeignClient feignClient;

    private static final Logger logger = LoggerFactory.getLogger(LicenseService.class);

    public List<License> findAll() {

        return licenseRepository.findAll();
    }

    public License getLicence(String licenseId, String organizationId) {

        License license =licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (license == null) {
            throw new IllegalArgumentException(
                    String.format(
                            messageSource.getMessage("license.search.error.message", null, null),
                            licenseId, organizationId));
        }
        return license.withComment(config.getProperty());
    }

    public License getLicense(String licenseId, String organizationId, String clientType) {

        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (license == null) {
            throw new IllegalArgumentException(
                    String.format(
                            messageSource.getMessage("license.search.error.message", null, null),
                            licenseId, organizationId));
        }

        Organization organization = retrieveOrganizationInfo(clientType, organizationId);
        if(organization != null) {
            license.setOrganizationName(organization.getName());
            license.setContactName(organization.getContactName());
            license.setContactEmail(organization.getContactEmail());
            license.setContactPhone(organization.getContactPhone());
        }

        return license.withComment(config.getProperty());
    }
    
    private Organization retrieveOrganizationInfo(String clientType, String organizationId) {

        Organization result = null;
        switch(clientType) {
            case "feign" -> {
                System.out.println("feign client");
                result = feignClient.getOrganization(organizationId);
            }
            case "discovery" -> {
                System.out.println("using discovery client");
                result = discoveryClient.getOrganization(organizationId);
            }
            case "rest" -> {
                System.out.println("using load balance rest template");
                result = restClient.getOrganization(organizationId);
            }
        }
        return result;
    }

    public License addLicence(License license, String organizationId, Locale locale) {
        license.setLicenseId(UUID.randomUUID().toString());
        license.setOrganizationId(organizationId);
        licenseRepository.save(license);

        return license.withComment(config.getProperty());
    }

    public License updateLicence(License license, String organizationId) {

        license.setOrganizationId(organizationId);
        licenseRepository.save(license);

        return license.withComment(config.getProperty());
    }

    public String deleteLicence(String licenseId) {

        licenseRepository.deleteById(licenseId);
        String responseMessage = String.format(
                messageSource.getMessage("license.delete.message", null, null), licenseId);

        return responseMessage;
    }

    /**
     *  bulk head: 處理併發數量
     *  rate limit: 限定秒數內的呼叫數量
     *  retry: 失敗後重複呼叫次數
     *  circuit breaker: 多次錯誤發生，若錯誤比例超過設定閥值就會阻止後續的呼叫，且執行後備方案
     */
    @CircuitBreaker(name = "licenseService", fallbackMethod = "buildFallBackList")
    @RateLimiter(name = "limitLicense", fallbackMethod = "buildRateFallBackList")
    @Retry(name = "retryLicense", fallbackMethod = "buildRetryFallBackList")
    @Bulkhead(name = "bulkheadLicense", fallbackMethod = "buildBulkFallBackList", type = Bulkhead.Type.SEMAPHORE)
    public List<License> findLicensesByOrganizationId(String organizationId) throws TimeoutException {

        logger.debug("findLicensesByOrganizationId Correlation Id: {}", UserContextHolder.getUserContext().getCorrelationId());
        randomlyRunLong();
        return licenseRepository.findByOrganizationId(organizationId);
    }

    private void randomlyRunLong() throws TimeoutException {
        Random random = new Random();
        int randomNum = random.nextInt(3) + 1;
        if(randomNum == 3) {
            sleep();
        }
    }


    private void sleep() throws TimeoutException {
        try {
            Thread.sleep(5000);
            throw new TimeoutException();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    private List<License> buildFallBackList(String organizationId, Throwable t) {

        System.out.println("build fallback: " + t.getMessage());
        List<License> result = new ArrayList<>();
        License license = new License();
        license.setLicenseId("00000000");
        license.setOrganizationId(organizationId);
        license.setProductName("unavailable");
        result.add(license);
        return result;
    }

    private List<License> buildBulkFallBackList(String organizationId, Throwable t) {

        System.out.println("build bulk head fallback: " + t.getMessage());
        List<License> result = new ArrayList<>();
        License license = new License();
        license.setLicenseId("1111111");
        license.setOrganizationId(organizationId);
        license.setProductName("bulk head test");
        result.add(license);
        return result;
    }

    private List<License> buildRetryFallBackList(String organizationId, Throwable t) {

        System.out.println("build retry: " + t.getMessage());
        List<License> result = new ArrayList<>();
        License license = new License();
        license.setLicenseId("222222");
        license.setOrganizationId(organizationId);
        license.setProductName("retry test");
        result.add(license);
        return result;
    }

    private List<License> buildRateFallBackList(String organizationId, Throwable t) {

        System.out.println("build rate limit: " + t.getMessage());
        List<License> result = new ArrayList<>();
        License license = new License();
        license.setLicenseId("33333");
        license.setOrganizationId(organizationId);
        license.setProductName("rate limit test");
        result.add(license);
        return result;
    }
}
