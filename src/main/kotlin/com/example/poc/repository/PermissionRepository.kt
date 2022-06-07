package com.example.poc.repository

import com.example.poc.domain.Permission
import io.micronaut.context.annotation.Executable
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface PermissionRepository : JpaRepository<Permission, Long> {
    @Executable
    fun findByTenantUserId(tenantUserId: Long): List<Permission>
}