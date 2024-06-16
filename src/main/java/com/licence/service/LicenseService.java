package com.licence.service;

import com.licence.config.ServiceConfig;
import com.licence.model.License;
import com.licence.repository.LicenseRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LicenseService {

    private final MessageSource messageSource;

    private final LicenseRepository licenseRepository;

    private final ServiceConfig config;


    public List<License> findAll() {

        return licenseRepository.findAll();
    }

    public License getLicence(String licenceId, String organizationId) {

        License license =licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenceId);
        if (license == null) {
            throw new IllegalArgumentException(
                    String.format(
                            messageSource.getMessage("license.search.error.message", null, null),
                            licenceId, organizationId));
        }
        return license.withComment(config.getProperty());
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

    public String deleteLicence(String licenceId) {

        licenseRepository.deleteById(licenceId);
        String responseMessage = String.format(
                messageSource.getMessage("license.delete.message", null, null), licenceId);

        return responseMessage;
    }
}
