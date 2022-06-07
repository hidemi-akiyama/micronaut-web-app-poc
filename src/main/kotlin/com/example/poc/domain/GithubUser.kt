package com.example.poc.domain

import io.micronaut.core.annotation.Introspected

@Introspected
data class GithubUser(val login: String, val name: String, val email: String?)
