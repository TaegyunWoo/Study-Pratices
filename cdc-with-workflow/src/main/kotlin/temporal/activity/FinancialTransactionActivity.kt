package org.ktor_lecture.temporal.activity

import io.temporal.activity.ActivityInterface
import io.temporal.activity.ActivityMethod
import org.ktor_lecture.temporal.workflow.TransactionRequest


@ActivityInterface
interface FinancialTransactionActivity {

    @ActivityMethod
    fun executeTransaction(request : TransactionRequest)
}