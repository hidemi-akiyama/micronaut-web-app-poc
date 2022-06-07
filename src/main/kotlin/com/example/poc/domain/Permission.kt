package com.example.poc.domain

import io.micronaut.core.annotation.Introspected
import javax.persistence.*

@Entity
class Permission(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false)
    val product: String,

    @Column(nullable = false)
    var permission: Boolean,

    @ManyToOne
    var tenantUser: TenantUser? = null
)
