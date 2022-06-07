package com.example.poc.controller

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/hello")
class HelloController {

    @Get
    @Produces(MediaType.TEXT_PLAIN)
    fun index(): String {
        return "Hello world"
    }
}
