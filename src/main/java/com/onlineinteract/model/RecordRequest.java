package com.onlineinteract.model;

import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Kafka Record Request generated from Client request indicating to backing service to lookup a record at a specific offset.
 * 
 * @author 330885096
 *
 */
public class RecordRequest {

    private String topicName;
    private int offset;
    private String uuid;
    private SimpMessagingTemplate template;

    public RecordRequest(String topicName, int offset, String uuid, SimpMessagingTemplate template) {
        this.topicName = topicName;
        this.offset = offset;
        this.uuid = uuid;
        this.template = template;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public SimpMessagingTemplate getTemplate() {
        return template;
    }

    public void setTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }
}
