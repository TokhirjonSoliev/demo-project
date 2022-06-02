package com.exadel.coolDesking.config.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic topic1() {
        return TopicBuilder.name("NotFoundException").build();
    }

//    @Bean
//    public NewTopic topic2() {
//        return TopicBuilder.name("NotFoundException").build();
//    }

    /*@Bean
    public NewTopic topic3() {
        return TopicBuilder.name("MyTopic").build();
    }

    @Bean
    public NewTopic topic4() {
        return TopicBuilder.name("MyTopic").build();
    }*/

}