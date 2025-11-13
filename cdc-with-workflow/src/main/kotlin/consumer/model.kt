package org.ktor_lecture.consumer

import com.fasterxml.jackson.annotation.JsonProperty
import org.ktor_lecture.temporal.workflow.TransactionRequest
import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(
    @JsonProperty("transaction_id")
    val transactionId: String,

    @JsonProperty("from_account_id")
    val fromAccountId: String?,

    @JsonProperty("to_account_id")
    val toAccountId: String?,

    @JsonProperty("transaction_type")
    val transactionType: TransactionType,

    @JsonProperty("amount")
    val amount: String,

    @JsonProperty("description")
    val description: String?,

    @JsonProperty("status")
    val status: TransactionStatus,

    @JsonProperty("created_at")
    val createdAt: LocalDateTime,

    @JsonProperty("updated_at")
    val updatedAt: LocalDateTime
) {

}


enum class TransactionType {
    @JsonProperty("DEPOSIT")
    DEPOSIT,

    @JsonProperty("WITHDRAWAL")
    WITHDRAWAL,

    @JsonProperty("TRANSFER")
    TRANSFER
}

enum class TransactionStatus {
    @JsonProperty("PENDING")
    PENDING,

    @JsonProperty("COMPLETED")
    COMPLETED,

    @JsonProperty("FAILED")
    FAILED
}

