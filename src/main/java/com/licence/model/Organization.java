package com.licence.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organization {

    String organizationId;
    String name;
    String contactName;
    String contactEmail;
    String contactPhone;

}
