package org.ktor_lecture.service

import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowOptions
import io.temporal.worker.Worker
import io.temporal.worker.WorkerFactory
import jakarta.annotation.PostConstruct
import org.ktor_lecture.config.TemporalConfig
import org.ktor_lecture.temporal.activity.FinancialTransactionActivityImpl
import org.ktor_lecture.temporal.workflow.FinancialTransactionWorkflowImpl
import org.ktor_lecture.temporal.workflow.FinancialTransactionWorkflow
import org.ktor_lecture.temporal.workflow.TransactionRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Duration


@Service
class TemporalWorkflowService(
    private val workflowClient: WorkflowClient,
    private val workerFactory: WorkerFactory,
    private val financialWorker: Worker,
    private val financialTransactionActivityImpl: FinancialTransactionActivityImpl
) {
    private val logger = LoggerFactory.getLogger(TemporalWorkflowService::class.java)


    @PostConstruct
    fun registerWorkflowAndActivites() {
        financialWorker.registerWorkflowImplementationTypes(FinancialTransactionWorkflowImpl::class.java)
        financialWorker.registerActivitiesImplementations(financialTransactionActivityImpl)

        runCatching {
            workerFactory.start()
            logger.info("Started workflow worker")
        }.onFailure { ex ->
            logger.error("Failed to start workflow worker", ex)
        }
    }

    fun startTransactionWorkflow(request : TransactionRequest) : String {
        return runCatching{
            val opts = WorkflowOptions.newBuilder()
                .setTaskQueue(TemporalConfig.FINANCIAL_TASK_QUEUE)
                .setWorkflowId("transaction-${request.transactionId}")
                .setWorkflowExecutionTimeout(Duration.ofMinutes(10))
                .build()

            val workflow = workflowClient.newWorkflowStub(FinancialTransactionWorkflow::class.java, opts)

            WorkflowClient.start(workflow::processTransaction, request)

            logger.info("workflow 시작됨!! transaction-${request.transactionId}")
            return "success"
        }.getOrElse { ex ->
            throw ex
        }

//        val opts = WorkflowOptions.newBuilder()
//            .setTaskQueue(TemporalConfig.FINANCIAL_TASK_QUEUE)
//            .setWorkflowId("transaction-${request.transactionId}")
//            .setWorkflowExecutionTimeout(Duration.ofMinutes(10))
//            .build()
//
//        val workflow = workflowClient.newWorkflowStub(FinancialTransactionWorkflow::class.java, opts)
//
//        WorkflowClient.start(workflow::processTransaction, request)
//
//        logger.info("workflow 시작됨!! transaction-${request.transactionId}")
//
//        return ""
    }

}