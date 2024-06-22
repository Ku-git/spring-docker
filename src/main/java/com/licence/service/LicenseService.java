package com.licence.service;

import com.licence.config.ServiceConfig;
import com.licence.model.License;
import com.licence.model.Organization;
import com.licence.repository.LicenseRepository;
import com.licence.service.client.OrganizationDiscoveryClient;
import com.licence.service.client.OrganizationFeignClient;
import com.licence.service.client.OrganizationRestTemplateClient;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LicenseService {

    private final MessageSource messageSource;

    private final LicenseRepository licenseRepository;

    private final ServiceConfig config;

    private final OrganizationDiscoveryClient discoveryClient;

    private final OrganizationRestTemplateClient restClient;

    private final OrganizationFeignClient feignClient;


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
}
