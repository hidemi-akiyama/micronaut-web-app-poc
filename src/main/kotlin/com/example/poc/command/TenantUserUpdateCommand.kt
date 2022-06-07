package com.example.poc.command

import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.Nullable
import javax.validation.constraints.NotBlank

@Introspected
data class TenantUserUpdateCommand(
    @field:NotBlank val id: Long,
    @field:NotBlank val name: String,
    @Nullable val email: String?
)
