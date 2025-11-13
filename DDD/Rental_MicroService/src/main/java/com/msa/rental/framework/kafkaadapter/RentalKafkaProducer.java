package com.msa.rental.framework.kafkaadapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.msa.rental.application.outputport.EventOutputPort;
import com.msa.rental.domain.model.event.ItemRented;
import com.msa.rental.domain.model.event.ItemReturned;
import com.msa.rental.domain.model.event.OverdueCleared;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class RentalKafkaProducer implements EventOutputPort {
    @Value("${producers.topic1.name}")
    private String TOPIC_RENT;
    @Value("${producers.topic2.name}")
    private String TOPIC_RETURN;
    @Value("${producers.topic3.name}")
    private String TOPIC_CLEAR;
    private final KafkaTemplate<String, ItemRented> kafkaTemplate1;
    private final KafkaTemplate<String, ItemReturned> kafkaTemplate2;
    private final KafkaTemplate<String, OverdueCleared> kafkaTemplate3;

    @Override
    public void occurRentalEvent(ItemRented itemRented) throws JsonProcessingException {
        ListenableFuture<SendResult<String, ItemRented>> future = kafkaTemplate1.send(TOPIC_RENT, itemRented);
        future.addCallback(result -> {
            ItemRented value = result.getProducerRecord().value();
            log.info("Sent message=[{}] with offset=[{}]", value.getItem().getNo(), result.getRecordMetadata().offset());
        }, ex -> {
            log.error("Unable to send message=[{}] due to : {}", itemRented.getItem().getNo(), ex.getMessage(), ex);
        });
    }

    @Override
    public void occurReturnEvent(ItemReturned itemReturned) throws JsonProcessingException {
        ListenableFuture<SendResult<String, ItemReturned>> future = this.kafkaTemplate2.send(TOPIC_RETURN, itemReturned);
        future.addCallback(result -> {
            ItemRented value = result.getProducerRecord().value();
            log.info("Sent message=[{}] with offset=[{}]", value.getItem().getNo(), result.getRecordMetadata().offset());
        }, ex -> {
            log.error("Unable to send message=[{}] due to : {}", itemReturned.getItem().getNo(), ex.getMessage(), ex);
        });
    }

    @Override
    public void occurOverdueClearedEvent(OverdueCleared overdueCleared) throws JsonProcessingException {
        ListenableFuture<SendResult<String, OverdueCleared>> future = this.kafkaTemplate3.send(TOPIC_CLEAR, overdueCleared);
        future.addCallback(result -> {
            OverdueCleared value = result.getProducerRecord().value();
            log.info("Sent message=[{}] with offset=[{}]", value.getIdName().getId(), result.getRecordMetadata().offset());
        }, ex -> {
            log.error("Unable to send message=[{}] due to : {}", overdueCleared.getIdName().getId(), ex.getMessage(), ex);
        });
    }
}


