package com.techtestinc.payment.services.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * @author yuvaraj.sanjeevi
 */
@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topic.online-payment}")
    private String onlineTopic;
    @Value("${spring.kafka.topic.offline-payment}")
    private String offlineTopic;

    @Bean
    public NewTopic onlineTopic(){
        return TopicBuilder.name(onlineTopic)
                .partitions(2)
                .build();
    }

    @Bean
    public NewTopic offlineTopic(){
        return TopicBuilder.name(offlineTopic)
                .partitions(2)
                .build();
    }

}
