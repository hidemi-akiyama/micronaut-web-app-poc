package com.example.poc.domain

import io.micronaut.core.annotation.Introspected

@Introspected
data class GithubRepo(val name: String)
