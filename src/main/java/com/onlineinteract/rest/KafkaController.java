package com.onlineinteract.rest;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineinteract.kafka.Processor;
import com.onlineinteract.model.RecordRequest;

/**
 * Service to handle Kafka Topic Monitoring requests.
 * 
 * @author 330885096
 *
 */
@Controller
@EnableAutoConfiguration
@EnableScheduling
public class KafkaController {
    private static final int FETCH_TOPIC_DETAIL_INTERVAL = 500;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String GLOBAL_TOPIC_DETAILS_TOPIC = "/topic/topicDetails";

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    Processor processor;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/token")
    @ResponseBody
    public String token(HttpServletRequest request, HttpServletResponse response) {
        logger.info("**** session timeout = " + request.getSession().getMaxInactiveInterval());
        // request.getSession().setMaxInactiveInterval(20);

        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        response.setHeader("X-CSRF-HEADER", token.getHeaderName());
        response.setHeader("X-CSRF-PARAM", token.getParameterName());
        response.setHeader("X-CSRF-TOKEN", token.getToken());
        return token.getToken();
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/session-timeout")
    @ResponseBody
    public String sessionTimeout(HttpServletRequest request, HttpServletResponse response) {
        logger.info("**** session timeout = " + request.getSession().getMaxInactiveInterval());
        return String.valueOf(request.getSession().getMaxInactiveInterval());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/reset-consumers")
    @ResponseBody
    public void resetConsumers(HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
        logger.info("resetConsumers() entry");
        processor.shutdownConsumers();
        Thread.sleep(4000);
        Processor.topics.clear();
        Processor.recordRequests.clear();
        processor.startConsumers();
        response.setStatus(HttpStatus.OK.value());
        logger.info("resetConsumers() exit");
    }

    /**
     * Fetch record details on a given topic/offset.
     * 
     * @param uuid the unique id from the client
     * @param request with topic info
     * @throws JsonProcessingException JsonProcessingException
     * @throws InterruptedException InterruptedException
     */
    @MessageMapping("/fetchRecord/{uuid}")
    public void fetchRecord(@DestinationVariable String uuid, Map<String, String> request) throws JsonProcessingException, InterruptedException {
        logger.info("uuid: " + uuid);
        RecordRequest recordRequest = new RecordRequest(request.get("topicName"), Integer.valueOf(request.get("offset")), uuid, template);
        Processor.recordRequests.add(recordRequest);
    }

    /**
     * Fetch all topic details and broadcast to all clients.
     * 
     * @throws JsonProcessingException JsonProcessingException
     */
    @Scheduled(fixedRate = FETCH_TOPIC_DETAIL_INTERVAL)
    public void fetchTopicDetails() throws JsonProcessingException {
        // logger.info("**** scheduler ****");
        try {
            ObjectMapper mapper = new ObjectMapper();
            Collections.sort(Processor.topics);
            String record = mapper.writeValueAsString(Processor.topics);
            this.template.convertAndSend(GLOBAL_TOPIC_DETAILS_TOPIC, record);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
    }

}
