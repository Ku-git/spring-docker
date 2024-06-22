package com.licence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class License {

    @Id
    private String licenseId;
    private String description;
    private String organizationId;
    private String productName;
    private String licenseType;
    @Transient
    private String comment;
    @Transient
    private String organizationName;
    @Transient
    private String contactName;
    @Transient
    private String contactEmail;
    @Transient
    private String contactPhone;

    public License withComment(String comment) {
        this.comment = comment;
        return this;
    }
}
