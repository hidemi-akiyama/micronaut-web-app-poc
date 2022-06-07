package com.example.poc.controller

import com.example.poc.command.TenantUserSaveCommand
import com.example.poc.command.TenantUserUpdateCommand
import com.example.poc.domain.TenantUser
import com.example.poc.repository.TenantUserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.micronaut.data.model.Page
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import jakarta.inject.Inject

@MicronautTest
class TenantUserApiControllerTest : AnnotationSpec() {
    @Inject
    @field:Client("/api")
    lateinit var client: HttpClient

    @Inject
    lateinit var tenantUserRepository: TenantUserRepository

    @BeforeEach
    @AfterEach
    fun cleanup() {
        tenantUserRepository.deleteAll()
    }

    @Test
    fun testFindNonExistingGenreReturn401() {
        val thrown = shouldThrow<HttpClientResponseException>{
            client.toBlocking().exchange<Any>("/genres/999999")
        }

        thrown.response.shouldNotBeNull()
        HttpStatus.UNAUTHORIZED.shouldBe(thrown.status)
    }

    @Test
    fun testTenantUserCRUDOperations() {
        // first user POST
        var postRequest = HttpRequest.POST("/tenant_users", TenantUserSaveCommand("test user", "test@example.com"))
        var postResponse = client.toBlocking().exchange(postRequest, TenantUser::class.java)

        val baseId = postResponse.body()?.id

        postResponse.status.shouldBe(HttpStatus.CREATED)
        postResponse.header(HttpHeaders.LOCATION).shouldBe("/tenant_users/${baseId}")

        // second user POST
        postRequest = HttpRequest.POST("/tenant_users", TenantUserSaveCommand("test user2", "test2@example.com"))
        postResponse = client.toBlocking().exchange(postRequest, TenantUser::class.java)

        val secondId = baseId?.plus(1)

        postResponse.status.shouldBe(HttpStatus.CREATED)
        postResponse.header(HttpHeaders.LOCATION).shouldBe("/tenant_users/${secondId}")

        // GET second user
        var tenantUser = client.toBlocking().retrieve("/tenant_users/${secondId}", TenantUser::class.java)

        tenantUser.name.shouldBe("test user2")
        tenantUser.email.shouldBe("test2@example.com")

        // PUT second user
        val putRequest = HttpRequest.PUT("/tenant_users/${secondId}",
            TenantUserUpdateCommand(tenantUser.id, "test updated user", "test2-1@example.com"))
        val putResponse = client.toBlocking().exchange(putRequest, TenantUser::class.java)

        putResponse.status.shouldBe(HttpStatus.NO_CONTENT)

        // Check updated user
        tenantUser = client.toBlocking().retrieve("/tenant_users/${secondId}", TenantUser::class.java)
        tenantUser.id.shouldBe(baseId?.plus(1))
        tenantUser.name.shouldBe("test updated user")
        tenantUser.email.shouldBe("test2-1@example.com")

        // Check list method
        client.toBlocking().retrieve("/tenant_users", Page::class.java).totalSize.shouldBe(2)

        // DELETE second user
        val delRequest: HttpRequest<TenantUser> = HttpRequest.DELETE("/tenant_users/${secondId}")
        val delResponse: HttpResponse<TenantUser> = client.toBlocking().exchange(delRequest)

        delResponse.status.shouldBe(HttpStatus.NO_CONTENT)

        // Check deleted user
        shouldThrow<HttpClientResponseException> {
            client.toBlocking().retrieve("/tenant_users/${secondId}", TenantUser::class.java)
        }

        // Check list after delete
        client.toBlocking().retrieve("/tenant_users", Page::class.java).totalSize.shouldBe(1)
    }
}
