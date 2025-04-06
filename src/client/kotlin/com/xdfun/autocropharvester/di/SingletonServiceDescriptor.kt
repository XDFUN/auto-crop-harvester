package com.xdfun.autocropharvester.di

class SingletonServiceDescriptor(
    private val factory: (ServiceProvider) -> Any
) : ServiceDescriptor {
    private var instance: Any? = null

    override fun get(serviceProvider: ServiceProvider): Any {
        return instance ?: factory(serviceProvider).also { instance = it }
    }
}