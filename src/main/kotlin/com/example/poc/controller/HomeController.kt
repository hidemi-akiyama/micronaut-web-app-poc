package com.example.poc.controller

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View

@Controller
class HomeController {

    @Secured(SecurityRule.IS_ANONYMOUS)
    @View("home")
    @Get
    fun index(): Map<String, Any> = HashMap()
}