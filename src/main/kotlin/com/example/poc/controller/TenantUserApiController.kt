package com.example.poc.controller

import com.example.poc.command.TenantUserSaveCommand
import com.example.poc.command.TenantUserUpdateCommand
import com.example.poc.domain.TenantUser
import com.example.poc.service.TenantUserService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View
import java.net.URI
import java.util.*
import javax.validation.Valid

@Secured(SecurityRule.IS_ANONYMOUS)
@ExecuteOn(TaskExecutors.IO)
@Controller("api/tenant_users")
class TenantUserApiController(private val tenantUserService: TenantUserService) {

    @Get("/")
    fun index(@Valid pageable: Pageable): Page<TenantUser> = tenantUserService.list(pageable)

    @Get("/{id}")
    fun show(id: Long): Optional<TenantUser> {
        return tenantUserService.findById(id)
    }

    @Post
    fun save(@Body @Valid command: TenantUserSaveCommand): HttpResponse<TenantUser> {
        val tenantUser = tenantUserService.save(command)

        return HttpResponse
            .created(tenantUser)
            .headers { headers -> headers.location(tenantUser.location) }
    }

    @Put("/{id}")
    fun update(@Body @Valid command: TenantUserUpdateCommand): HttpResponse<TenantUser> {
        val tenantUser = tenantUserService.update(command)

        return HttpResponse
            .noContent<TenantUser>()
            .header(HttpHeaders.LOCATION, tenantUser.location.path)
    }

    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT)
    fun delete(id: Long) = tenantUserService.deletedById(id)

    private val Long?.location: URI
        get() = URI.create("/tenant_users/$this")

    private val TenantUser.location: URI
        get() = id.location
}
