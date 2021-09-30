package com.ewan.ciboard.global.exception.exceptions

import com.ewan.ciboard.global.exception.ErrorCode

class CustomException(private val errorCode: ErrorCode) : RuntimeException(errorCode.message)
