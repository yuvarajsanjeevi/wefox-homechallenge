package com.techtestinc.payment.services.listener;

import com.techtestinc.payment.services.enums.PaymentType;
import com.techtestinc.payment.services.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yuvaraj.sanjeevi
 */
@Service
@EnableKafka
@Slf4j
@AllArgsConstructor
public class PaymentListener {

    private final PaymentService paymentService;

    @KafkaListener(topics = "${spring.kafka.topic.online-payment}")
    public void processOnlinePayment(@Payload List<String> payloads,
                                     @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                                     @Header(KafkaHeaders.OFFSET) List<Long> offsets
    ) {
        for (int i = 0; i < payloads.size(); i++) {
            String payload = payloads.get(i);
            log.info("Received online payment message='{}' with partition-offset='{}'", payloads.get(i), partitions.get(i) + "-" + offsets.get(i));
            paymentService.processPayment(payload, PaymentType.ONLINE);
        }
        log.info("All online payment messages processed successfully");
    }

    @KafkaListener(topics = "${spring.kafka.topic.offline-payment}")
    public void processOfflinePayment(@Payload List<String> payloads,
                                      @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                                      @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        for (int i = 0; i < payloads.size(); i++) {
            String payload = payloads.get(i);
            log.info("Received offline payment message='{}' with partition-offset='{}'", payloads.get(i), partitions.get(i) + "-" + offsets.get(i));
            paymentService.processPayment(payload, PaymentType.OFFLINE);
        }
        log.info("All offline payment messages processed successfully");
    }
}
