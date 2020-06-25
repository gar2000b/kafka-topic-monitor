package com.onlineinteract.kafka;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.avro.generic.GenericData.Record;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineinteract.config.ApplicationProperties;
import com.onlineinteract.config.KafkaConfig;
import com.onlineinteract.model.RecordRequest;
import com.onlineinteract.model.TopicConfiguration;
import com.onlineinteract.model.TopicDetail;

@Component("consumer")
public class Processor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String EMPTY = "";

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String OUT_OF_RANGE_MSG = "The offset requested appears to be out of range or there was an issue, "
                    + "please ensure you are requesting a record within the last 7 days or try again.";
    private static final String INITIAL_DATE = "2018-01-01 00:00:00";
    private static final String LOCAL_ENV = "l";

    private boolean runningFlag = true;

    public static int counter = 0;
    public static String lastValue = EMPTY;
    public static String lastValueTime = EMPTY;
    public static List<TopicDetail> topics = new ArrayList<>();
    public static List<RecordRequest> recordRequests = new ArrayList<>();

    @Autowired
    ApplicationProperties applicationProperties;

    @Autowired
    KafkaConfig kafkaConfig;

    /**
     * Startup all Consumers.
     */
    @PostConstruct
    public void startConsumers() {
        runningFlag = true;
        createAndExecuteConsumers();
    }

    /**
     * Create and execute consumers for each and every topic configuration set.<br>
     * If running locally, predefined topic configurations specified in the constants above will be used.<br>
     * If running on any cloud environment,the topic configurations will be obtained from UPS.
     */
    private void createAndExecuteConsumers() {
        Map<String, String> topicConfigurations = new HashMap<>();
        if (applicationProperties.getApplicationEnvCode().equals(LOCAL_ENV)) {
            topicConfigurations.put(KafkaConfig.LOCAL_TEST_TOPIC, KafkaConfig.LOCAL_TEST_TOPIC_CONFIGURATION);
            topicConfigurations.put(KafkaConfig.LOCAL_EXAMPLE_TOPIC, KafkaConfig.LOCAL_EXAMPLE_TOPIC_CONFIGURATION);
		}

        logger.info("**** topicConfigurations length is: " + topicConfigurations.size() + " ****");
        for (Map.Entry<String, String> entry : topicConfigurations.entrySet()) {
            logger.info("**** registering topic construct ****");
            TopicConfiguration topicConfiguration = null;
            ObjectMapper mapper = new ObjectMapper();
            try {
                topicConfiguration = mapper.readValue(entry.getValue(), TopicConfiguration.class);
                logger.info("**** mapper called, yielding: " + topicConfiguration.getTopic() + " ****");
            } catch (IOException e) {
                e.printStackTrace();
            }

            Properties buildProperties = kafkaConfig.buildLocalConsumerProperties(topicConfiguration);
            KafkaConsumer<String, ?> consumer = new KafkaConsumer<>(buildProperties);
            consumer.subscribe(Arrays.asList(entry.getKey()));
            registerTopics(topicConfiguration.getTopic(), topicConfiguration.getConsumerGroup(), topicConfiguration.getEnvironment(), 0, EMPTY, INITIAL_DATE);
            logger.info("*** registering topic - " + topicConfiguration.getTopic());
            processRecords(topicConfiguration, consumer);
        }
    }

    /**
     * All topics (with initial settings) are registered/set against the local topics cache for future reference.
     * 
     * @param topic topic
     * @param consumerGroup consumerGroup
     * @param environment environment
     * @param offset offset
     * @param payload payload
     * @param latestPayloadDateTime latestPayloadDateTime
     */
    private void registerTopics(String topic, String consumerGroup, String environment, long offset, String payload, String latestPayloadDateTime) {
        TopicDetail topicDetail = constructTopicDetail(topic, consumerGroup, environment, offset, payload, latestPayloadDateTime);
        topics.add(topicDetail);
    }

    /**
     * Processes each each consumer in parallel.<br>
     * 1. fetches the records.<br>
     * 2. gets timestamp + payload for each record.<br>
     * 3. Updates the local topic cache with the latest information.
     * 
     * @param topicConfiguration topicConfiguration
     * @param consumer consumer
     */
    private void processRecords(TopicConfiguration topicConfiguration, KafkaConsumer<String, ?> consumer) {
        logger.info("polling " + topicConfiguration.getTopic() + " " + runningFlag);

        new Thread(() -> {
            boolean fetchLatestFlag = false;
            while (runningFlag) {
                try {
                    fetchRecords(consumer, topicConfiguration);
                    ConsumerRecords<String, ?> records = consumer.poll(100);
                    for (ConsumerRecord<String, ?> consumerRecord : records) {
                        logger.info("Processing record at offset: " + consumerRecord.offset());
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
                        long timestamp = consumerRecord.timestamp();
                        String payload = EMPTY;
                        payload = consumerRecord.value().toString();
                        logger.info("Constructing topic detail for record at offset: " + consumerRecord.offset());
                        TopicDetail topicDetail =
                                        constructTopicDetail(topicConfiguration.getTopic(), topicConfiguration.getConsumerGroup(), topicConfiguration.getEnvironment(), consumerRecord.offset(),
                                                        payload, sdf.format(timestamp));
                        updateTopic(topicDetail);
                        logger.info("* Kafka Record Topic: " + topicConfiguration.getTopic() + " record at offset: " + consumerRecord.offset() + " at timestamp: " + consumerRecord.timestamp()
                                        + " the record is: " + consumerRecord.value());
                    }
                } catch (Throwable e) {
                    logger.error("Problem Processing Records: " + e.getMessage());
                    // resetConsumers();
                }
                if (!fetchLatestFlag) {
                    logger.info("Fetching latest record entry");
                    fetchLatestRecord(consumer, topicConfiguration);
                    logger.info("Fetching latest record exit");
                    fetchLatestFlag = !fetchLatestFlag;
                }
            }

            consumer.unsubscribe();
            consumer.close();
            // shutdownConsumers();
        }).start();
    }

    /**
     * Checks for fetch-record requests from the client and returns the record value detail if it finds it based on offset.
     * 
     * @param consumer kafka consumer
     * @param topicConfiguration configuration
     */
    private void fetchRecords(KafkaConsumer<String, ?> consumer, TopicConfiguration topicConfiguration) {
        if (recordRequests.size() > 0) {
            logger.info("fetching records, recordsRequests size: " + recordRequests.size());
        }

        for (int i = recordRequests.size() - 1; i > -1; i--) {
            RecordRequest recordRequest = recordRequests.get(i);

            if (recordRequest.getTopicName().equals(topicConfiguration.getTopic())) {
                Set<TopicPartition> topicPartitions = consumer.assignment();
                for (TopicPartition topicPartition : topicPartitions) {
                    if (recordRequest.getOffset() >= 0) {
                        consumer.seek(topicPartition, recordRequest.getOffset());
                    } else {
                        consumer.seek(topicPartition, 0);
                    }
                }

                ConsumerRecords<String, ?> customerRecords = consumer.poll(750);
                String record = "";
                try {
                    if (customerRecords.iterator().hasNext()) {
                        ConsumerRecord<String, ?> consumerRecord = customerRecords.iterator().next();
                        record = consumerRecord.value().toString();
                        logger.info("**** record fetched for topic: " + topicConfiguration.getTopic() + " is: " + record + " at offset: " + consumerRecord.offset() + " inserted at time: "
                                        + consumerRecord.timestamp() + " ****");
                    } else {
                        record = OUT_OF_RANGE_MSG;
                    }
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
                consumer.seekToEnd(topicPartitions);

                recordRequest.getTemplate().convertAndSend("/topic/record/" + recordRequest.getUuid(), record);
                recordRequests.remove(i);
            }
        }
    }

    private void fetchLatestRecord(KafkaConsumer<String, ?> consumer, TopicConfiguration topicConfiguration) {
        Set<TopicPartition> topicPartitions = consumer.assignment();
        consumer.seekToEnd(topicPartitions);
        for (TopicPartition topicPartition : topicPartitions) {
            long lastOffset = consumer.position(topicPartition) - 1;
            if (lastOffset >= 0)
                consumer.seek(topicPartition, lastOffset);
        }

        ConsumerRecords<String, ?> customerRecords = consumer.poll(750);
        String record = "";
        ConsumerRecord<String, ?> consumerRecord = null;
        try {
            if (customerRecords.iterator().hasNext()) {
                consumerRecord = customerRecords.iterator().next();
                record = consumerRecord.value().toString();
                logger.info("**** record fetched for topic: " + topicConfiguration.getTopic() + " is: " + record + " at offset: " + consumerRecord.offset() + " inserted at time: "
                                + consumerRecord.timestamp() + " ****");
            } else {
                record = OUT_OF_RANGE_MSG;
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
        TopicDetail topicDetail = null;
        if (consumerRecord != null) {
            topicDetail = constructTopicDetail(topicConfiguration.getTopic(), topicConfiguration.getConsumerGroup(), topicConfiguration.getEnvironment(), consumerRecord.offset(),
                            record, sdf.format(consumerRecord.timestamp()));
            updateTopic(topicDetail);
        }
        consumer.seekToEnd(topicPartitions);
    }

    /**
     * Updates the specified topic (contained within the topic detail) in the local topic cache with the latest response detail.
     * 
     * @param topicDetail topicDetail
     */
    private synchronized void updateTopic(TopicDetail topicDetail) {
        for (TopicDetail td : topics) {
            if (td.getName().equals(topicDetail.getName())) {
                td.setLatestPayloadDateTime(topicDetail.getLatestPayloadDateTime());
                td.setOffset(topicDetail.getOffset());
                td.setPayload(topicDetail.getPayload());
            }
        }
    }

    /**
     * Constructs topic detail type with details passed in.
     * 
     * @param topic topic
     * @param consumerGroup consumerGroup
     * @param environment environment
     * @param offset offset
     * @param payload payload
     * @param latestPayloadDateTime latestPayloadDateTime
     * @return topicDetail
     */
    private TopicDetail constructTopicDetail(String topic, String consumerGroup, String environment, long offset, String payload,
                    String latestPayloadDateTime) {
        TopicDetail topicDetail = new TopicDetail();
        topicDetail.setName(topic);
        topicDetail.setConsumerGroup(consumerGroup);
        topicDetail.setEnvironment(environment);
        topicDetail.setOffset(offset);
        topicDetail.setLatestPayloadDateTime(latestPayloadDateTime);
        topicDetail.setPayload(payload);
        return topicDetail;
    }

    /*
     * private void resetConsumers() { try { shutdownConsumers(); Thread.sleep(4000); topics.clear(); recordRequests.clear(); startConsumers(); } catch (InterruptedException e) {
     * logger.error("Reseting consumers: " + e.getMessage()); } }
     */

    @PreDestroy
    public void shutdownConsumers() {
        logger.info("*** consumers shutting down");
        runningFlag = false;
    }
}
