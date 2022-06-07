package com.example.poc.service

import com.example.poc.command.TenantUserSaveCommand
import com.example.poc.command.TenantUserUpdateCommand
import com.example.poc.domain.TenantUser
import com.example.poc.repository.TenantUserRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import java.util.Optional

@Singleton
class TenantUserService(
    private val tenantUserRepository: TenantUserRepository) {

    fun list(pageable: Pageable): Page<TenantUser> = tenantUserRepository.findAll(pageable)

    fun findById(id: Long): Optional<TenantUser> = tenantUserRepository.findById(id)

    fun save(command: TenantUserSaveCommand): TenantUser {
        return tenantUserRepository.save(TenantUser(0, command.name, command.email))
    }

    fun update(command: TenantUserUpdateCommand): TenantUser {
        return tenantUserRepository.update(TenantUser(command.id, command.name, command.email))
    }

    fun deletedById(id: Long) = tenantUserRepository.deleteById(id)
}
