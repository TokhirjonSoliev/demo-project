package com.exadel.coolDesking.config.kafka;

import com.exadel.coolDesking.common.exception.ExceptionAdvice;
import com.exadel.coolDesking.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final ExceptionAdvice exceptionAdvice;

    @KafkaListener(topics = "NotFoundException", groupId = "groupId1", containerFactory = "listenerContainerFactory")
    void listener(NotFoundException notFoundException) {
        exceptionAdvice.handleNotFoundException(notFoundException);
        System.out.println("Listener received: " + notFoundException.getMessage());
//        System.out.println("Yessssssssssssssssss");
    }

    /*@KafkaListener(topics = "MyTopic", groupId = "groupId")
    void listener(String data) {
        System.out.println("Listener received: " + data);
    }*/


}
