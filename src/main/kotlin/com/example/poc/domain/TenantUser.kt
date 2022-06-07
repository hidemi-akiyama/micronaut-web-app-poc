package com.example.poc.domain

import javax.persistence.*

@Entity
class TenantUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = true)
    var email: String? = null,

    @OneToMany(mappedBy = "tenantUser", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var permissions: List<Permission>? = null
)