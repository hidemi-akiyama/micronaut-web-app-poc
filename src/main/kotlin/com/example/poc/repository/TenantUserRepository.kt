package com.example.poc.repository

import com.example.poc.domain.TenantUser
import io.micronaut.context.annotation.Executable
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.Optional

@Repository
interface TenantUserRepository : JpaRepository<TenantUser, Long> {
    @Executable
    fun findByName(name: String): Optional<TenantUser>
    @Executable
    fun findByEmail(email: String): Optional<TenantUser>
}