package com.onlineinteract.model;

public class TopicDetail implements Comparable<TopicDetail> {
    String name;
    String consumerGroup;
    String environment;
    long offset;
    String payload;
    String latestPayloadDateTime;

    public TopicDetail() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getLatestPayloadDateTime() {
        return latestPayloadDateTime;
    }

    public void setLatestPayloadDateTime(String latestPayloadDateTime) {
        this.latestPayloadDateTime = latestPayloadDateTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TopicDetail other = (TopicDetail) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public int compareTo(TopicDetail o) {
        if (this.getEnvironment().charAt(0) > o.getEnvironment().charAt(0)) {
            return 1;
        } else if (this.getEnvironment().charAt(0) == o.getEnvironment().charAt(0)) {
            return 0;
        } else {
            return -1;
        }
    }
}
