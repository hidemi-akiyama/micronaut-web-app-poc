package com.example.poc.controller

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.nulls.shouldNotBeNull
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import jakarta.inject.Inject

@MicronautTest
class HelloControllerFromClientTest : AnnotationSpec() {
    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun testHello() {
        val request: HttpRequest<Any> = HttpRequest.GET("/hello")
        val body = client.toBlocking().retrieve(request)

        body.shouldNotBeNull()
        body.shouldBeEqualComparingTo("Hello world")
    }
}
