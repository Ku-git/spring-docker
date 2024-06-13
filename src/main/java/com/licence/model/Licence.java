package com.licence.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class Licence {

    private int id;
    private String licenceId;
    private String description;
    private String organizationId;
    private String productName;
    private String licenceType;

}
