package com.licence.service;

import com.licence.model.Licence;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Random;

@Service
public class LicenceService {

    private final MessageSource messageSource;

    private LicenceService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public Licence getLicence(String licenceId, String organizationId) {

        Licence licence = Licence.builder()
                .id(new Random().nextInt(1000))
                .licenceId(licenceId)
                .organizationId(organizationId)
                .description("Software product")
                .productName("Ostock")
                .licenceType("full")
                .build();
        return licence;
    }

    public String addLicence(Licence licence, String organizationId, Locale locale) {
        String responseMessage = null;
        if(licence != null) {
            licence.setOrganizationId(organizationId);
            String message = messageSource.getMessage("license.create.message", null, locale);
            responseMessage = String.format(message, licence);
        }

        return responseMessage;
    }

    public String updateLicence(Licence licence, String organizationId) {
        String responseMessage = null;
        if(licence != null) {
            licence.setOrganizationId(organizationId);
            String message = messageSource.getMessage("license.update.message", null, null);
            responseMessage = String.format(message, licence.toString());
        }

        return responseMessage;
    }

    public String deleteLicence(String licenceId, String organizationId) {

        String responseMessage = String.format("deleting licence with id %s for the organization %s",
                    licenceId, organizationId);

        return responseMessage;
    }
}
