package com.detectify.codingchallenge.webdriverservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public NewTopic createScreenshotTopic() {
        return new NewTopic("create-screenshot", 1, (short) 1);
    }

    @Bean
    public NewTopic screenshotTakenTopic() {
        return new NewTopic("screenshot-taken", 1, (short) 1);
    }

}
