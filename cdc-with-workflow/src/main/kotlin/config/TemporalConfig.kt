package org.ktor_lecture.config

import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowClientOptions
import io.temporal.serviceclient.WorkflowServiceStubs
import io.temporal.serviceclient.WorkflowServiceStubsOptions
import io.temporal.worker.Worker
import io.temporal.worker.WorkerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class TemporalConfig {
    @Value("\${temporal.connection.target}")
    private lateinit var temporalTarget: String

    @Value("\${temporal.namespace}")
    private lateinit var temporalNamespace: String // dev, alpha, stg, prod, default


    companion object {
        const val FINANCIAL_TASK_QUEUE = "FINANCIAL_TASK_QUEUE"
    }

    @Bean
    fun workflowServiceStubs() : WorkflowServiceStubs {
        return WorkflowServiceStubs.newServiceStubs(
            WorkflowServiceStubsOptions.newBuilder()
                .setTarget(temporalTarget)
                .build()
        )
    }

    @Bean
    fun workflowClient(workflowServiceStubs : WorkflowServiceStubs) : WorkflowClient {
        return WorkflowClient.newInstance(
            workflowServiceStubs,
            WorkflowClientOptions.newBuilder()
                .setNamespace(temporalNamespace)
                .build()
        )
    }

    @Bean
    fun workFactory(workClient: WorkflowClient) : WorkerFactory {
        val factory = WorkerFactory.newInstance(workClient)
        return factory
    }

    @Bean
    fun financialWorker(workerFactory: WorkerFactory) : Worker {
        return workerFactory.newWorker(FINANCIAL_TASK_QUEUE)
    }

}