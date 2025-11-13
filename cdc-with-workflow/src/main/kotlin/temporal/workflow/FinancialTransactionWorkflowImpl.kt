package org.ktor_lecture.temporal.workflow

import io.temporal.activity.ActivityOptions
import io.temporal.common.RetryOptions
import io.temporal.workflow.Workflow
import org.ktor_lecture.temporal.activity.FinancialTransactionActivity
import org.slf4j.LoggerFactory
import java.time.Duration

class FinancialTransactionWorkflowImpl : FinancialTransactionWorkflow {
    private val logger = LoggerFactory.getLogger(FinancialTransactionWorkflowImpl::class.java)
    private val opts = ActivityOptions.newBuilder()
        .setStartToCloseTimeout(Duration.ofMinutes(5))
        .setRetryOptions(
            RetryOptions.newBuilder()
                .setMaximumAttempts(3)
                .build()
        ).build()

    private val activites = Workflow.newActivityStub(FinancialTransactionActivity::class.java, opts)

    override fun processTransaction(request: TransactionRequest): TransactionResult {
        try {
            logger.info("processing transaction ${request.transactionId}")
            activites.executeTransaction(request)
            logger.info("finished transaction ${request.transactionId}")

            return TransactionResult (
                transactionId = request.transactionId,
                status = TransactionStatus.COMPLETED,
                message = "거래 성공"
            )
        } catch (ex : Exception) {
            logger.error(ex.message, ex)

            return TransactionResult (
                transactionId = request.transactionId,
                status = TransactionStatus.FAILED,
                message = "거래 실패: ${ex.message}"
            )
        }

    }
}