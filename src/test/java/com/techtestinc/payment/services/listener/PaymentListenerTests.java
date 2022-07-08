package com.techtestinc.payment.services.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techtestinc.payment.services.listener.PaymentListener;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

/**
 * @author yuvaraj.sanjeevi
 */
@ExtendWith(SpringExtension.class)
@EmbeddedKafka(brokerProperties = {"listeners=PLAINTEXT://localhost:9096", "port=9096"})
@SpringBootTest(properties =  {"spring.kafka.bootstrap-servers=localhost:9096"})
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PaymentListenerTests {

    private static final String TOPIC_ONLINE = "online";
    private static final String TOPIC_OFFLINE = "offline";
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @SpyBean
    private PaymentListener paymentListener;

    @Autowired
    private ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<List<String>> payloadArgumentCaptor;

    @Captor
    ArgumentCaptor<List<Integer>> partitionArgumentCaptor;

    @Captor
    ArgumentCaptor<List<Long>> offsetArgumentCaptor;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @BeforeAll
    public void setUp() {
        this.kafkaTemplate = buildKafkaTemplate();
        for (final MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry.getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(messageListenerContainer, embeddedKafkaBroker.getPartitionsPerTopic());
        }
    }

    private KafkaTemplate<String, String> buildKafkaTemplate() {
        Map<String, Object> senderProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        ProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(senderProps);
        return new KafkaTemplate<>(pf);
    }

    @Test
    void test_processOnlinePayment() throws JSONException {
        String message = "[{\"payment_id\": \"3f5379b6-4a95-49b3-a9c0-3a5703df8073\", \"account_id\": 547, \"payment_type\": \"online\", \"credit_card\": \"4980276372331233\", \"amount\": 30}]";
        kafkaTemplate.send(TOPIC_ONLINE, message);


        verify(paymentListener, timeout(5000).times(1))
                .processOnlinePayment(payloadArgumentCaptor.capture(), partitionArgumentCaptor.capture(), offsetArgumentCaptor.capture());
        assertEquals(1, payloadArgumentCaptor.getValue().size());
        JSONAssert.assertEquals(message, payloadArgumentCaptor.getValue().get(0), JSONCompareMode.LENIENT);

    }

    @Test
    void test_processOfflinePayment() throws JSONException {
        String message = "[{\"payment_id\": \"3f5379b6-4a95-49b3-a9c0-3a5703df8073\", \"account_id\": 547, \"payment_type\": \"offline\", \"credit_card\": \"4980276372331233\", \"amount\": 30}]";
        kafkaTemplate.send(TOPIC_OFFLINE, message);


        verify(paymentListener, timeout(5000).times(1))
                .processOfflinePayment(payloadArgumentCaptor.capture(), partitionArgumentCaptor.capture(), offsetArgumentCaptor.capture());

        assertEquals(1, payloadArgumentCaptor.getValue().size());
        JSONAssert.assertEquals(message, payloadArgumentCaptor.getValue().get(0), JSONCompareMode.LENIENT);

    }


}
