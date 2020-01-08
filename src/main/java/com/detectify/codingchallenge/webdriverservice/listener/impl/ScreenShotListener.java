package com.detectify.codingchallenge.webdriverservice.listener.impl;

import com.detectify.codingchallenge.webdriverservice.dto.ScreenShotDto;
import com.detectify.codingchallenge.webdriverservice.listener.Listener;
import com.detectify.codingchallenge.webdriverservice.service.WebDriverService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@AllArgsConstructor
@Slf4j
public class ScreenShotListener implements Listener<String> {

    private static final String SCREENSHOT_TAKEN = "screenshot-taken";

    private final WebDriverService webDriverService;
    private final KafkaTemplate<String, ScreenShotDto> kafkaTemplate;

    @Override
    @KafkaListener(groupId = "web-driver-service", topics = {"create-screenshot"})
    public void listen(String topic, String url) {
        log.info("Take screen shot request for url [{}]", url);
        byte[] image = webDriverService.takeScreenShot(url);
        log.info("Screen shot taken for URL:[{}]", url);
        ScreenShotDto screenShotDto = new ScreenShotDto(url, image);
        kafkaTemplate.send(SCREENSHOT_TAKEN, screenShotDto)
                .addCallback(o -> handleSuccess(SCREENSHOT_TAKEN, screenShotDto),
                        o -> handleFailure(SCREENSHOT_TAKEN, screenShotDto, o));
    }

    private void handleSuccess(String topic, Object payload) {
        log.info("Message {} was sent to the topic {} successfully!", payload, topic);
    }

    private void handleFailure(String topic, Object payload, Throwable ex) {
        String msg = MessageFormat.format("Message {0} was not sent to the topic {1}", payload, topic);
        log.error(msg, ex);
    }
}
