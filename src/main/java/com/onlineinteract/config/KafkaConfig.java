package com.onlineinteract.config;

import java.util.Properties;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.onlineinteract.model.TopicConfiguration;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;

@Component
public class KafkaConfig {

    /**
     * The following configuration constants are for testing locally. All other configurations will be read from UPS.
     */
	
	public static final String LOCAL_TEST_TOPIC = "test-topic";
    public static final String LOCAL_TEST_TOPIC_CONFIGURATION = "{\"topic\":\"test-topic\", \"consumerGroup\":\"test-topic-monitor\"}";
	
	
	
    public static final String UAT_TOPIC_LOCAL = "bw00-u-pda-opened-event";
    public static final String IT_TOPIC_LOCAL = "bw00-d-i-pda-opened-event";
    public static final String DEV_TOPIC_LOCAL = "bw00-d-pda-opened-event";
    public static final String QAT_TOPIC_LOCAL = "bw00-q-pda-opened-event";
    public static final String UAT_TOPIC_DIRECT_DEPOSIT_LOCAL = "bw00-u-pda-direct-deposit-event";
    public static final String IT_TOPIC_DIRECT_DEPOSIT_LOCAL = "bw00-d-i-pda-direct-deposit-event";
    public static final String DEV_TOPIC_DIRECT_DEPOSIT_LOCAL = "bw00-d-pda-direct-deposit-event";
    public static final String QAT_TOPIC_DIRECT_DEPOSIT_LOCAL = "bw00-q-pda-direct-deposit-event";
    public static final String UAT_TOPIC_PRE_AUTH_LOCAL = "bw00-u-pda-pre-auth-payment-event";
    public static final String IT_TOPIC_PRE_AUTH_LOCAL = "bw00-d-i-pda-pre-auth-payment-event";
    public static final String DEV_TOPIC_PRE_AUTH_LOCAL = "bw00-d-pda-pre-auth-payment-event";
    public static final String QAT_TOPIC_PRE_AUTH_LOCAL = "bw00-q-pda-pre-auth-payment-event";

    public static final String DEV_TOPIC_LOCAL_CONFIGURATION = "{\"topic\":\"bw00-d-pda-opened-event\", \"consumerGroup\":\"bw00-d-pda-opened-event-monitor\", "
                    + "\"environment\":\"DEV\", \"jaasConsumerConf\":\"com.sun.security.auth.module.Krb5LoginModule required doNotPrompt=true "
                    + "refreshKrb5Config=true useKeyTab=true storeKey=true keyTab='{keytabFile}' principal='SZGV0SRVDKPDIDOPNKMR@SAIFG.abc.COM';\", "
                    + "\"keytabFilename\":\"SZGV0SRVDKPDIDOPNKMR.keytab\", \"keytab\":\"BQIAAABUAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWREtQRElET1BOS01SAA"
                    + "AAAFttv8gBABIAIBRzlOAwLYpnI23GWXOMzeev2ujb5pINAB15SHVYpHuvAAAARAABAA1TQUlGRy5SQkMuQ09NABRTWkdWMFNSVkRLUERJRE9QTktNUgAAAABbbb/IA"
                    + "QARABB5TyZXZ5m78KMsamQ6/fkhAAAATAABAA1TQUlGRy5SQkMuQ09NABRTWkdWMFNSVkRLUERJRE9QTktNUgAAAABbbb/IAQAQABgxa8KKy+bc98ffrdO1yw0vj0Yv"
                    + "06LCTNAAAABEAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWREtQRElET1BOS01SAAAAAFttv8gBABcAEG/QPUpSExopNhMlh+Dh+g0=\"}";
    public static final String QAT_TOPIC_LOCAL_CONFIGURATION = "{\"topic\":\"bw00-q-pda-opened-event\", \"consumerGroup\":\"bw00-q-pda-opened-event-monitor\", "
                    + "\"environment\":\"QAT\", \"jaasConsumerConf\":\"com.sun.security.auth.module.Krb5LoginModule required doNotPrompt=true "
                    + "refreshKrb5Config=true useKeyTab=true storeKey=true keyTab='{keytabFile}' principal='SZGV0SRVQKPDIDOPNKMR@SAIFG.abc.COM';\", "
                    + "\"keytabFilename\":\"SZGV0SRVQKPDIDOPNKMR.keytab\", \"keytab\":\"BQIAAABUAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWUUtQRElET1BOS01SAAA"
                    + "AAFttv+UBABIAIKuFyzEGmdtux6fYeGWeE91NeADIlpzXOlXjMsfEFdTFAAAARAABAA1TQUlGRy5SQkMuQ09NABRTWkdWMFNSVlFLUERJRE9QTktNUgAAAABbbb/lAQA"
                    + "RABBa2gMh74gYIvWae5MGSHJxAAAATAABAA1TQUlGRy5SQkMuQ09NABRTWkdWMFNSVlFLUERJRE9QTktNUgAAAABbbb/lAQAQABgmHCCAtrw+363vkW7QYoDcoXomhv3"
                    + "s3zQAAABEAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWUUtQRElET1BOS01SAAAAAFttv+UBABcAEO0CnIWdptsPVOBBS1QpUTM=\"}";
    public static final String IT_TOPIC_LOCAL_CONFIGURATION = "{\"topic\":\"bw00-d-i-pda-opened-event\", \"consumerGroup\":\"bw00-d-i-pda-opened-event-monitor\", "
                    + "\"environment\":\"IT\", \"jaasConsumerConf\":\"com.sun.security.auth.module.Krb5LoginModule required doNotPrompt=true "
                    + "refreshKrb5Config=true useKeyTab=true storeKey=true keyTab='{keytabFile}' principal='SZGV0SRVIKPDIDOPNKMR@SAIFG.abc.COM';\", "
                    + "\"keytabFilename\":\"SZGV0SRVIKPDIDOPNKMR.keytab\", \"keytab\":\"BQIAAABUAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWSUtQRElET1BOS01SAAAA"
                    + "AFttwBYBABIAIDB12zk+Hny7E6T8POY0Xkftm3Fo6Thjs4DvLpHcwVQ3AAAARAABAA1TQUlGRy5SQkMuQ09NABRTWkdWMFNSVklLUERJRE9QTktNUgAAAABbbcAWAQARA"
                    + "BAFXI5mIGpGZlF5f+283wQ+AAAATAABAA1TQUlGRy5SQkMuQ09NABRTWkdWMFNSVklLUERJRE9QTktNUgAAAABbbcAWAQAQABgNUTeRkq5d9xz+1UCeB/cfYp0E8s55Q2"
                    + "IAAABEAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWSUtQRElET1BOS01SAAAAAFttwBYBABcAEKTKloire7t6Fvr+55UIlNE=\"}";
    public static final String UAT_TOPIC_LOCAL_CONFIGURATION = "{\"topic\":\"bw00-u-pda-opened-event\", \"consumerGroup\":\"bw00-u-pda-opened-event-monitor\", "
                    + "\"environment\":\"UAT\", \"jaasConsumerConf\":\"com.sun.security.auth.module.Krb5LoginModule required doNotPrompt=true "
                    + "refreshKrb5Config=true useKeyTab=true storeKey=true keyTab='{keytabFile}' principal='SZGV0SRVUKPDIDOPNKMR@SAIFG.abc.COM';\", "
                    + "\"keytabFilename\":\"SZGV0SRVUKPDIDOPNKMR.keytab\", \"keytab\":\"BQIAAABUAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWVUtQRElET1BOS01SAAAAA"
                    + "Ftt2bYBABIAIK5OWZIm0OBMMfFy1Vnac7FrEF0BodTmoTsfvo9KBBymAAAARAABAA1TQUlGRy5SQkMuQ09NABRTWkdWMFNSVlVLUERJRE9QTktNUgAAAABbbdm2AQARABB"
                    + "OoIL9oOd2403GELneOcvHAAAATAABAA1TQUlGRy5SQkMuQ09NABRTWkdWMFNSVlVLUERJRE9QTktNUgAAAABbbdm2AQAQABiDzrw0yJ6P9NkvXfR2dfSUZ4y2uZG5XssAA"
                    + "ABEAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWVUtQRElET1BOS01SAAAAAFtt2bYBABcAEMo9VAU3/o/obLMGrFvL6YA=\"}";
    public static final String DEV_TOPIC_DIRECT_DEPOSIT_CONFIGURATION =
                    "{\"topic\":\"bw00-d-pda-direct-deposit-event\", \"consumerGroup\":\"bw00-d-pda-direct-deposit-event-monitor\", \"environment\":\"DEV\", "
                    + "\"jaasConsumerConf\":\"com.sun.security.auth.module.Krb5LoginModule required doNotPrompt=true refreshKrb5Config=true useKeyTab=true "
                    + "storeKey=true keyTab='{keytabFile}' principal='SZGV0SRVDKPDEDDIRKMR@SAIFG.abc.COM';\", \"keytabFilename\":\"SZGV0SRVDKPDEDDIRKMR.keytab\", "
                    + "\"keytab\":\"BQIAAABIAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWREtQREVERElSS01SAAAAAVxi9wMBABcA EFfaCn6tZy4D6Nyows6ELJkAAAAB\"}";
    public static final String QAT_TOPIC_DIRECT_DEPOSIT_CONFIGURATION =
                    "{\"topic\":\"bw00-q-pda-direct-deposit-event\", \"consumerGroup\":\"bw00-q-pda-direct-deposit-event-monitor\", \"environment\":\"QAT\", "
                    + "\"jaasConsumerConf\":\"com.sun.security.auth.module.Krb5LoginModule required doNotPrompt=true refreshKrb5Config=true useKeyTab=true "
                    + "storeKey=true keyTab='{keytabFile}' principal='SZGV0SRVQKPDEDDIRKMR@SAIFG.abc.COM';\", \"keytabFilename\":\"SZGV0SRVQKPDEDDIRKMR.keytab\", "
                    + "\"keytab\":\"BQIAAABUAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWUUtQREVERElSS01SAAAAAFxcZRkBABIAIEBn1jlpc3g4xP/tYYaWtjgzgm0KR0yR+W9v462q/"
                    + "i2AAAAARAABAA1TQUlGRy5SQkMuQ09NABRTWkdWMFNSVlFLUERFRERJUktNUgAAAABcXGUZAQARABDTd9ZX4z1UatBCeZsgcQhnAAAATAABAA1TQUlGRy5SQkMuQ09NABRT"
                    + "WkdWMFNSVlFLUERFRERJUktNUgAAAABcXGUZAQAQABiFdnBbwpcHxwhtXvIQUTgcV0PQT3UmVBoAAABEAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWUUtQREVERElSS01SA"
                    + "AAAAFxcZRkBABcAEBLHge7USJ5GP9I3kTcXbvU=\"}";
    public static final String IT_TOPIC_DIRECT_DEPOSIT_CONFIGURATION =
                    "{\"topic\":\"bw00-d-i-pda-direct-deposit-event\", \"consumerGroup\":\"bw00-d-i-pda-direct-deposit-event-monitor\", \"environment\":\"IT\", "
                    + "\"jaasConsumerConf\":\"com.sun.security.auth.module.Krb5LoginModule required doNotPrompt=true refreshKrb5Config=true useKeyTab=true "
                    + "storeKey=true keyTab='{keytabFile}' principal='SZGV0SRVIKPDEDDIRKMR@SAIFG.abc.COM';\", \"keytabFilename\":\"SZGV0SRVIKPDEDDIRKMR.keytab\", "
                    + "\"keytab\":\"BQIAAABUAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWSUtQREVERElSS01SAAAA AFxch6EBABIAIIECm8nbBGhcf9a5kwuX+jKuRVVFeWJk7OjT4yNLI90b"
                    + "AAAARAAB AA1TQUlGRy5SQkMuQ09NABRTWkdWMFNSVklLUERFRERJUktNUgAAAABcXIehAQAR ABCADhZnpAZlSHjC4KDXMCHQAAAATAABAA1TQUlGRy5SQkMuQ09NABRTWkdWMFNS "
                    + "VklLUERFRERJUktNUgAAAABcXIehAQAQABjNxHX0bokc/RXLQxZFwc7Vxz5PBwSU uuAAAABEAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWSUtQREVERElSS01SAAAA AFxch6E"
                    + "BABcAENFUehE+LtD49JaKkdXurLk=\"}";
    public static final String UAT_TOPIC_DIRECT_DEPOSIT_CONFIGURATION =
                    "{\"topic\":\"bw00-u-pda-direct-deposit-event\", \"consumerGroup\":\"bw00-u-pda-direct-deposit-event-monitor\", \"environment\":\"UAT\", "
                    + "\"jaasConsumerConf\":\"com.sun.security.auth.module.Krb5LoginModule required doNotPrompt=true refreshKrb5Config=true useKeyTab=true "
                    + "storeKey=true keyTab='{keytabFile}' principal='SZGV0SRVUKPDEDDIRKMR@SAIFG.abc.COM';\", \"keytabFilename\":\"SZGV0SRVUKPDEDDIRKMR.keytab\", "
                    + "\"keytab\":\"BQIAAABUAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWVUtQREVERElSS01SAAAAAFxdnA0BABIAIIkdAoGsgO/d6Jy1hJjZobpEIJ4mpJs6jrh+lB7jb79EAAAA"
                    + "RAABAA1TQUlGRy5SQkMuQ09NABRTWkdWMFNSVlVLUERFRERJUktNUgAAAABcXZwNAQARABC2RpU4IL0jFpi80m66RUegAAAATAABAA1TQUlGRy5SQkMuQ09NABRTWkdWMFNSVlVLU"
                    + "ERFRERJUktNUgAAAABcXZwNAQAQABiFbvjIc/IjqL8HqE96WNk7f4CwYsQCSbUAAABEAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWVUtQREVERElSS01SAAAAAFxdnA0BABcAEK"
                    + "C/1EIbuLmqsLVz4Xs4fz8=\"}";
    public static final String DEV_TOPIC_PRE_AUTH_CONFIGURATION =
                    "{\"topic\":\"bw00-d-pda-pre-auth-payment-event\", \"consumerGroup\":\"bw00-d-pda-pre-auth-payment-event-monitor\", \"environment\":\"DEV\", "
                    + "\"jaasConsumerConf\":\"com.sun.security.auth.module.Krb5LoginModule required doNotPrompt=true refreshKrb5Config=true useKeyTab=true "
                    + "storeKey=true keyTab='{keytabFile}' principal='SZGV0SRVDKPDEDPAPKMR@SAIFG.abc.COM';\", \"keytabFilename\":\"SZGV0SRVDKPDEDPAPKMR.keytab\", "
                    + "\"keytab\":\"BQIAAABIAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWREtQREVEUEFQS01SAAAAAVxfIjABABcA EEvUW+29ac1Fui8pLOfbYQQAAAAB\"}";
    public static final String QAT_TOPIC_PRE_AUTH_CONFIGURATION =
                    "{\"topic\":\"bw00-q-pda-pre-auth-payment-event\", \"consumerGroup\":\"bw00-q-pda-pre-auth-payment-event-monitor\", \"environment\":\"QAT\", "
                    + "\"jaasConsumerConf\":\"com.sun.security.auth.module.Krb5LoginModule required doNotPrompt=true refreshKrb5Config=true useKeyTab=true "
                    + "storeKey=true keyTab='{keytabFile}' principal='SZGV0SRVQKPDEDPAPKMR@SAIFG.abc.COM';\", \"keytabFilename\":\"SZGV0SRVQKPDEDPAPKMR.keytab\", "
                    + "\"keytab\":\"BQIAAABUAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWUUtQREVEUEFQS01SAAAAAFxcgTQBABIAIIaom6/xOMP/9zIIofp9MsHQlhqCQJnd8aLK7TPlmr"
                    + "ALAAAARAABAA1TQUlGRy5SQkMuQ09NABRTWkdWMFNSVlFLUERFRFBBUEtNUgAAAABcXIE0AQARABB0YQeHylT+Z1dn5LEHNek2AAAATAABAA1TQUlGRy5SQkMuQ09NABRTWkd"
                    + "WMFNSVlFLUERFRFBBUEtNUgAAAABcXIE0AQAQABh2Q63pyHXvPf5F8uAOeQhXH4BbLMjcayAAAABEAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWUUtQREVEUEFQS01SAAAAAF"
                    + "xcgTQBABcAEKFYzV/FHpCozyIwCKOBPOQ=\"}";
    public static final String IT_TOPIC_PRE_AUTH_CONFIGURATION =
                    "{\"topic\":\"bw00-d-i-pda-pre-auth-payment-event\", \"consumerGroup\":\"bw00-d-i-pda-pre-auth-payment-event-monitor\", "
                    + "\"environment\":\"IT\", \"jaasConsumerConf\":\"com.sun.security.auth.module.Krb5LoginModule required doNotPrompt=true "
                    + "refreshKrb5Config=true useKeyTab=true storeKey=true keyTab='{keytabFile}' principal='SZGV0SRVIKPDEDPAPKMR@SAIFG.abc.COM';\", "
                    + "\"keytabFilename\":\"SZGV0SRVIKPDEDPAPKMR.keytab\", \"keytab\":\"BQIAAABUAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWSUtQREVEUEFQS01SAAAAA"
                    + "FxcfpoBABIAIPhFNaWiSi992AnEjqsR4HGo1wHA6r42YgKRdz2WvKCXAAAARAABAA1TQUlGRy5SQkMuQ09NABRTWkdWMFNSVklLUERFRFBBUEtNUgAAAABcXH6aAQARABC"
                    + "1WFmi9Z3io5wwpZRBQLlNAAAATAABAA1TQUlGRy5SQkMuQ09NABRTWkdWMFNSVklLUERFRFBBUEtNUgAAAABcXH6aAQAQABhMRozZpAvqZCqGXjHchuXyvFcOzoDTehYAA"
                    + "ABEAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWSUtQREVEUEFQS01SAAAAAFxcfpoBABcAEOXPQbfDWrDisFPQT+B6e54=\"}";
    public static final String UAT_TOPIC_PRE_AUTH_CONFIGURATION =
                    "{\"topic\":\"bw00-u-pda-pre-auth-payment-event\", \"consumerGroup\":\"bw00-u-pda-pre-auth-payment-event-monitor\", \"environment\":\"UAT\", "
                    + "\"jaasConsumerConf\":\"com.sun.security.auth.module.Krb5LoginModule required doNotPrompt=true refreshKrb5Config=true useKeyTab=true "
                    + "storeKey=true keyTab='{keytabFile}' principal='SZGV0SRVUKPDEDPAPKMR@SAIFG.abc.COM';\", \"keytabFilename\":\"SZGV0SRVUKPDEDPAPKMR.keytab\", "
                    + "\"keytab\":\"BQIAAABUAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWVUtQREVEUEFQS01SAAAAAFxdmxoBABIAIBtswPfwHFZJYRjEF+j3qkbAZ3mhgnRaX5WuDb7zorNEAAAA"
                    + "RAABAA1TQUlGRy5SQkMuQ09NABRTWkdWMFNSVlVLUERFRFBBUEtNUgAAAABcXZsaAQARABDPlk/XTMgebNJ88p9D6FrDAAAATAABAA1TQUlGRy5SQkMuQ09NABRTWkdWMFNSVlVL"
                    + "UERFRFBBUEtNUgAAAABcXZsaAQAQABirgO8qYRNSba6GIF3EXiCkTAExGpLVidMAAABEAAEADVNBSUZHLlJCQy5DT00AFFNaR1YwU1JWVUtQREVEUEFQS01SAAAAAFxdmxoBABcA"
                    + "ECaKEZw2B8ZJlmsfRamn5VE=\"}";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ApplicationProperties applicationProperties;

    public Properties buildLocalConsumerProperties(TopicConfiguration topicConfiguration) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers",
                        applicationProperties.getBootstrapServers());
        logger.info("^^^ " + applicationProperties.getBootstrapServers());
        properties.put("group.id", topicConfiguration.getConsumerGroup());
        logger.info("^^^ " + topicConfiguration.getConsumerGroup());
        properties.put("enable.auto.commit", "false");
        properties.put("max.poll.records", "200");
        properties.put("key.deserializer", StringDeserializer.class);
        properties.put("value.deserializer", KafkaAvroDeserializer.class);
        properties.put("schema.registry.url", applicationProperties.getSchemaRegistryUrl());

        return properties;
    }
}
