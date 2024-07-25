package com.licence.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrganizationEventListener {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationEventListener.class);


    @KafkaListener(topics = "${action.operation.change.topic}", groupId = "organization")
    public void listen(ConsumerRecord<String, String> record) {

        logger.info("received message: {}", record);
        ObjectMapper objectMapper = new ObjectMapper();
        OrganizationChangeModel organization = null;
        try {
            organization = objectMapper.readValue(record.value(), new TypeReference<OrganizationChangeModel>() {});
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        if(organization == null) {
            logger.info("organization has not been found");
            return;
        }

        String action = organization.getAction();
        switch (action) {
            case "GET":
                logger.debug("Received a GET event from the organization service for organization id {}", organization.getOrganizationId());
                break;
            case "CREATE":
                logger.debug("Received a CREATE event from the organization service for organization id {}", organization.getOrganizationId());
                break;
            case "UPDATE":
                logger.debug("Received a UPDATE event from the organization service for organization id {}", organization.getOrganizationId());
                break;
            case "DELETE":
                logger.debug("Received a DELETE event from the organization service for organization id {}", organization.getOrganizationId());
                break;
            default:
                logger.error("Received an UNKNOWN event from the organization service of type {}", organization.getType());
                break;
        }


    }
}
