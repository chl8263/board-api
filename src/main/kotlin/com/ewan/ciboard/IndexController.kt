package com.ewan.ciboard

import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController


@RestController
class IndexController {
    @GetMapping("/")
    fun index(): String {
        return "index"
    }

    @GetMapping("/admin")
    fun admin(): String? {
        return "어드민 페이지입니다."
    }
}