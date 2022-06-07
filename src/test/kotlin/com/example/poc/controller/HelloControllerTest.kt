package com.example.poc.controller

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.nulls.shouldNotBeNull
import io.micronaut.test.extensions.kotest.annotation.MicronautTest

@MicronautTest
class HelloControllerTest(
    private var helloController: HelloController
): StringSpec({
    "testHello" {
        val body = helloController.index()

        body.shouldNotBeNull()
        body.shouldBeEqualComparingTo("Hello world")
    }
})
