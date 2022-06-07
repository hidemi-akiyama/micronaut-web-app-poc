package com.example.poc.client

import com.example.poc.domain.GithubRepo
import com.example.poc.domain.GithubUser
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpHeaders
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Headers
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import org.reactivestreams.Publisher
import javax.validation.constraints.Pattern

@Headers(
    Header(name = "User-Agent", value = "https://micronautguides.com"),
    Header(name = "Accept", value = "application/vnd.github.v3+json, application/json")
)
@Client(id = "githubv3")
interface GithubApiClient {

    @Get("/user")
    fun getUser(@Header(HttpHeaders.AUTHORIZATION)
                authorization: String?): Publisher<GithubUser>

    @Get("/user/repos{?sort,direction}")
    fun repos(
        @Pattern(regexp = "created|updated|pushed|full_name") @Nullable @QueryValue sort: String?,
        @Pattern(regexp = "asc|desc") @Nullable @QueryValue direction: String?,
        @Header(HttpHeaders.AUTHORIZATION) authorization: String?): List<GithubRepo>
}
