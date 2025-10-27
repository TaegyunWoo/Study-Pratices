package org.ktor_lecture.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import org.ktor_lecture.service.TemporalWorkflowService
import org.ktor_lecture.temporal.workflow.TransactionRequest
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.nio.ByteBuffer
import java.util.Base64

@Component
class KafkaEventConsumer(
    private val objectMapper: ObjectMapper,
    private val temporalWorkflowService: TemporalWorkflowService
) {
    private val logger = LoggerFactory.getLogger(KafkaEventConsumer::class.java)

    @KafkaListener( topics = ["\${app.kafka.topics.customers}"])
    fun consumeEvents(
        @Payload message: String,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partition: Int,
        @Header(KafkaHeaders.OFFSET) offset : Long,
        ack: Acknowledgment
    ) {
        logger.info("Consuming topic $topic")
        ack.acknowledge()
    }

    @KafkaListener( topics = ["\${app.kafka.topics.accounts}"])
    fun consumeAccountEvents(
        @Payload message: String,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partition: Int,
        @Header(KafkaHeaders.OFFSET) offset : Long,
        ack: Acknowledgment
    ) {
        logger.info("Consuming topic $topic")
        ack.acknowledge()
    }

    @KafkaListener( topics = ["\${app.kafka.topics.transactions}"])
    fun consumeTransactionEvents(
        @Payload message: String,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partition: Int,
        @Header(KafkaHeaders.OFFSET) offset : Long,
        ack: Acknowledgment
    ) {
        logger.info("message : $message")

        val value = objectMapper.readValue(message, Transaction::class.java)
        logger.info("converted value $value")

        startWorkflow(value)
        ack.acknowledge()
    }

    private fun startWorkflow(e : Transaction) {
        val transactionRequest = TransactionRequest(
            transactionId = e.transactionId,
            fromAccountId = e.fromAccountId,
            toAccountId = e.toAccountId,
            transactionType = e.transactionType.toString(),
            description = e.description,
            amount = decodeAmount(e.amount),
        )

        temporalWorkflowService.startTransactionWorkflow(transactionRequest)
    }


    private fun decodeAmount(encodedAmount: String?) : BigDecimal {
        return try {
            if (encodedAmount.isNullOrBlank()) {
                BigDecimal.ZERO
            } else {
                val decodedBytes = Base64.getDecoder().decode(encodedAmount)
                val buffer = ByteBuffer.wrap(decodedBytes)

                return BigDecimal(buffer.long)
            }
        } catch (e : Exception) {
            BigDecimal.ZERO
        }
    }
}