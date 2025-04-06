package com.xdfun.autocropharvester.di

import java.util.Stack
import kotlin.reflect.KClass
import kotlin.reflect.KType

/*
* A very rudimentary dependency injection implementation
*/
class ServiceCollection {
    companion object {
        val Instance = ServiceCollection()
    }

    private val services = mutableMapOf<KClass<*>, Stack<ServiceDescriptor>>()

    fun addSingleton(serviceType: KClass<*>, factory: (ServiceProvider) -> Any): ServiceCollection {
        services.computeIfAbsent(serviceType) {
            Stack<ServiceDescriptor>()
        }.add(SingletonServiceDescriptor(factory))

        return this
    }

    fun createServiceProvider(): ServiceProvider {
        val providerMap = mutableMapOf<KClass<*>, Stack<ServiceDescriptor>>()

        // creating a readonly copy of the current services
        services.forEach { (key, stack) ->
            val newStack = Stack<ServiceDescriptor>()
            providerMap[key] = newStack

            stack.forEach { provider -> newStack.push(provider) }
        }

        return ServiceProvider(providerMap)
    }
}