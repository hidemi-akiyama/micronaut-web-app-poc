package com.example.poc.repository

import com.example.poc.domain.Permission
import com.example.poc.domain.TenantUser
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.micronaut.context.BeanContext
import io.micronaut.data.annotation.Query
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import jakarta.inject.Inject

@MicronautTest
class TenantUserRepositoryTest : AnnotationSpec() {
    @Inject
    lateinit var beanContext: BeanContext

    @Inject
    lateinit var tenantUserRepository: TenantUserRepository

    @Test
    fun testAnnotationMetadata() {
        val query = beanContext.getBeanDefinition(TenantUserRepository::class.java) //
            .getRequiredMethod<Any>("findByName", String::class.java) //
            .annotationMetadata
            .stringValue(Query::class.java) //
            .orElse(null)

        query.shouldBeEqualComparingTo(
            "SELECT tenantUser_ FROM com.example.poc.domain.TenantUser AS tenantUser_ WHERE (tenantUser_.name = :p1)"
        )
    }

    @Test
    fun testCRUD() {
        // Initial
        tenantUserRepository.findByName("test rep user").orElse(null).shouldBeNull()

        // CREATE
        val tenantUser = TenantUser(0, "test rep user", "hoge@example.com")
        tenantUserRepository.save(tenantUser)
        tenantUser.permissions = listOf(
            Permission(0, "accounting", true, tenantUser),
            Permission(0, "invoice", false, tenantUser)
        )
        tenantUserRepository.flush()

        // READ
        var saved = tenantUserRepository.findByName("test rep user").orElse(null)
        saved.name.shouldBe("test rep user")
        saved.email.shouldBe("hoge@example.com")

        saved = tenantUserRepository.findByEmail("hoge@example.com").orElse(null)
        saved.name.shouldBe("test rep user")
        saved.email.shouldBe("hoge@example.com")

        // UPDATE
        saved.email = "fuga@example.com"
        tenantUserRepository.flush()
        val updated = tenantUserRepository.findByName("test rep user").orElse(null)
        updated.id.shouldBe(saved.id)
        updated.name.shouldBe("test rep user")
        updated.email.shouldBe("fuga@example.com")

        // DELETE
        tenantUserRepository.delete(updated)
        tenantUserRepository.findByName("test rep user").orElse(null).shouldBeNull()
    }

    @Test
    fun testOneToManyRelationForPermission() {
        val tenantUser1 = tenantUserRepository.saveAndFlush(TenantUser(0, "user1"))
        val tenantUser2 = tenantUserRepository.saveAndFlush(TenantUser(0, "user2"))
        tenantUser1.permissions = listOf(
            Permission(0, "accounting", true, tenantUser1),
            Permission(0, "invoice", false, tenantUser1)
        )
        tenantUser2.permissions = listOf(
            Permission(0, "tax", true, tenantUser2)
        )
        tenantUserRepository.flush()

        val tenantUser1FromDb = tenantUserRepository.findByName("user1").orElse(null)
        val tenantUser2FromDb = tenantUserRepository.findByName("user2").orElse(null)
        val tenantUser1RelatedPermissions = tenantUser1FromDb.permissions
        val tenantUser2RelatedPermissions = tenantUser2FromDb.permissions

        tenantUser1RelatedPermissions?.size.shouldBe(2)
        tenantUser2RelatedPermissions?.size.shouldBe(1)
        tenantUser1RelatedPermissions?.equals(tenantUser1.permissions)?.shouldBeTrue()
        tenantUser2RelatedPermissions?.equals(tenantUser2.permissions)?.shouldBeTrue()
    }
}