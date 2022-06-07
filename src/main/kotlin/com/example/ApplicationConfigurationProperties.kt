package com.example

import io.micronaut.context.annotation.ConfigurationProperties
import javax.validation.constraints.NotBlank

@ConfigurationProperties("application")
class ApplicationConfigurationProperties {
    @NotBlank
    val max: Int = 10
}