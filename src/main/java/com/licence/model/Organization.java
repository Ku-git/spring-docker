package com.licence.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("organization")
public class Organization {

    @Id
    String organizationId;
    String name;
    String contactName;
    String contactEmail;
    String contactPhone;

}
