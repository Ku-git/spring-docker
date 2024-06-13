package com.licence.controller;

import com.licence.model.Licence;
import com.licence.service.LicenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("v1/organization/{organizationId}/licence")
public class LicenceController {

    private final LicenceService licenceService;

    public LicenceController(LicenceService licenceService) {
        this.licenceService = licenceService;
    }

    @GetMapping("/{licenceId}")
    public ResponseEntity<Licence> getLicence(@PathVariable("organizationId") String organizationId,
                                              @PathVariable("licenceId") String licenceId) {

        Licence licence = licenceService.getLicence(licenceId, organizationId);
        return ResponseEntity.ok(licence);
    }

    @PostMapping
    public ResponseEntity<String> addLicence(@PathVariable("organizationId") String organizationId,
         @RequestBody Licence licence, @RequestHeader(value = "Accept-Language",required = false) Locale locale) {

        String result = licenceService.addLicence(licence, organizationId, locale);
        return ResponseEntity.ok(result);
    }

    @PutMapping
    public ResponseEntity<String> updateLicence(@PathVariable("organizationId") String organizationId,
                                                @RequestBody Licence licence) {

        String result = licenceService.updateLicence(licence, organizationId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{licenceId}")
    public ResponseEntity<String> deleteLicence(@PathVariable("organizationId") String organizationId,
                                                @PathVariable("licenceId") String licenceId) {

        String result = licenceService.deleteLicence(licenceId, organizationId);
        return ResponseEntity.ok(result);
    }

}
