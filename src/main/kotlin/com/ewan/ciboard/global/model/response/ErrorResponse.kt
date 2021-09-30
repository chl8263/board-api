package com.ewan.ciboard.global.model.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.validation.FieldError
import java.time.LocalDateTime

class ErrorResponse(var status: Int,
                    var code: String,
                    var message: String,
                    private var fieldErrors: List<FieldError> = arrayListOf()) {
    val dateTimeStamp: String = (LocalDateTime.now()?: "00:00:00").toString()

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("errors")
    val customFieldErrors: MutableList<CustomFieldError> = arrayListOf()

    private fun convertFieldErrorsToCustomFieldErrors(fieldErrors: List<FieldError>) {
        fieldErrors.forEach {
            customFieldErrors.add(CustomFieldError(
                field = it.codes?.get(0)?: "",
                value = it.rejectedValue?: "",
                reason = it.defaultMessage?: ""
            ))
        }
    }
    init {
        convertFieldErrorsToCustomFieldErrors(fieldErrors)
    }
}