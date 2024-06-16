package com.licence.controller;

import com.licence.model.License;
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

    @GetMapping("/{licenceId}")
    public ResponseEntity<License> getLicence(@PathVariable("organizationId") String organizationId,
                                              @PathVariable("licenceId") String licenceId) {

        License license = licenseService.getLicence(licenceId, organizationId);
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

    @DeleteMapping("{licenceId}")
    public ResponseEntity<String> deleteLicence(@PathVariable("organizationId") String organizationId,
                                                @PathVariable("licenceId") String licenceId) {

        String result = licenseService.deleteLicence(licenceId);
        return ResponseEntity.ok(result);
    }

}
