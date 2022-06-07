package com.example

//import io.micronaut.context.ApplicationContextBuilder
//import io.micronaut.context.ApplicationContextConfigurer
//import io.micronaut.context.annotation.ContextConfigurer
import io.micronaut.runtime.Micronaut.*
//import io.swagger.v3.oas.annotations.*
//import io.swagger.v3.oas.annotations.info.*

/*
@OpenAPIDefinition(
    info = Info(
            title = "micronaut-web-app-poc",
            version = "0.0"
    )
)
object Api {
}

@ContextConfigurer
class DefaultEnvironmentConfigurer: ApplicationContextConfigurer {
	override fun configure(builder: ApplicationContextBuilder) {
			builder.defaultEnvironments("dev");
	}
}
*/

fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("com.example")
		.start()
}

