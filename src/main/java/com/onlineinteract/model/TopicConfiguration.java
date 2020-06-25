package com.onlineinteract.model;

public class TopicConfiguration {

    String topic;
    String consumerGroup;
    String environment;
    String jaasConsumerConf;
    String keytabFilename;
    String keytab;

    public TopicConfiguration() {
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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

    public String getJaasConsumerConf() {
        return jaasConsumerConf;
    }

    public void setJaasConsumerConf(String jaasConsumerConf) {
        this.jaasConsumerConf = jaasConsumerConf;
    }

    public String getKeytabFilename() {
        return keytabFilename;
    }

    public void setKeytabFilename(String keytabFilename) {
        this.keytabFilename = keytabFilename;
    }

    public String getKeytab() {
        return keytab;
    }

    public void setKeytab(String keytab) {
        this.keytab = keytab;
    }
}
