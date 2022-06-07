package com.example.poc.repository

import com.example.poc.domain.Permission
import com.example.poc.domain.TenantUser
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.shouldBe
import io.micronaut.context.BeanContext
import io.micronaut.data.annotation.Query
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import jakarta.inject.Inject

@MicronautTest
class PermissionRepositoryTest : AnnotationSpec() {
    @Inject
    lateinit var beanContext: BeanContext

    @Inject
    lateinit var tenantUserRepository: TenantUserRepository

    @Inject
    lateinit var permissionRepository: PermissionRepository

    @Test
    fun testAnnotationMetadata() {
        val query = beanContext.getBeanDefinition(PermissionRepository::class.java) //
            .getRequiredMethod<Any>("findByTenantUserId", Long::class.java) //
            .annotationMetadata
            .stringValue(Query::class.java) //
            .orElse(null)

        query.shouldBeEqualComparingTo(
            "SELECT permission_ FROM com.example.poc.domain.Permission AS permission_ WHERE (permission_.tenantUser.id = :p1)"
        )
    }

    @Test
    fun testCRUD() {
        // CREATE
        val tenantUser = tenantUserRepository.saveAndFlush(TenantUser(0, "user"))
        val permission = Permission(0, "accounting", true, tenantUser)
        permissionRepository.saveAndFlush(permission)

        // READ
        val saved = permissionRepository.findByTenantUserId(tenantUser.id)
        val permissionFromDb = saved[0]

        saved.size.shouldBe(1)
        permissionFromDb.tenantUser?.id.shouldBe(tenantUser.id)
        permissionFromDb.product.shouldBe("accounting")
        permissionFromDb.permission.shouldBeTrue()

        // UPDATE
        permissionFromDb.permission = false
        permissionRepository.update(permissionFromDb)
        val updated = permissionRepository.findByTenantUserId(tenantUser.id)
        val updatedPermissionFromDb = updated[0]

        updated.size.shouldBe(1)
        updatedPermissionFromDb.id.shouldBe(permissionFromDb.id)
        updatedPermissionFromDb.tenantUser?.id.shouldBe(tenantUser.id)
        updatedPermissionFromDb.product.shouldBe("accounting")
        updatedPermissionFromDb.permission.shouldBeFalse()

        // DELETE
        permissionRepository.delete(updatedPermissionFromDb)
        permissionRepository.findByTenantUserId(tenantUser.id).size.shouldBe(0)
    }
}