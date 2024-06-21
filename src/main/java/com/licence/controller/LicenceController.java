package com.licence.controller;

import com.licence.model.License;
import com.licence.model.Organization;
import com.licence.service.LicenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("v1/organization/{organizationId}/license")
public class LicenceController {

    private final LicenseService licenseService;

    public LicenceController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @GetMapping("/{licenseId}")
    public ResponseEntity<License> getLicence(@PathVariable("organizationId") String organizationId,
                                              @PathVariable("licenseId") String licenseId) {

        License license = licenseService.getLicence(licenseId, organizationId);
        return ResponseEntity.ok(license);
    }
    
    @GetMapping("/{licenseId}/{clientType}")
    public ResponseEntity<License> getLicenseWithClient(@PathVariable("organizationId") String organizationId,
                                                        @PathVariable("licenseId") String licenseId,
                                                        @PathVariable("clientType") String clientType) {

        License license = licenseService.getLicense(licenseId, organizationId, clientType);
        return ResponseEntity.ok(license);
    }

    @GetMapping("/")
    public ResponseEntity<List<License>> findAll(@PathVariable("organizationId") String organizationId) {

        List<License> licenses = licenseService.findAll();
        return ResponseEntity.ok(licenses);
    }

    @PostMapping
    public ResponseEntity<License> addLicence(@PathVariable("organizationId") String organizationId,
                                             @RequestBody License license, @RequestHeader(value = "Accept-Language",required = false) Locale locale) {

        License result = licenseService.addLicence(license, organizationId, locale);
        return ResponseEntity.ok(result);
    }

    @PutMapping
    public ResponseEntity<License> updateLicence(@PathVariable("organizationId") String organizationId,
                                                @RequestBody License license) {

        License result = licenseService.updateLicence(license, organizationId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{licenseId}")
    public ResponseEntity<String> deleteLicence(@PathVariable("organizationId") String organizationId,
                                                @PathVariable("licenseId") String licenseId) {

        String result = licenseService.deleteLicence(licenseId);
        return ResponseEntity.ok(result);
    }

}
