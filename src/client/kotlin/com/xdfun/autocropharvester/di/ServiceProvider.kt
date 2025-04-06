package com.xdfun.autocropharvester.di

import java.util.Stack
import kotlin.reflect.KClass

/*
* A very rudimentary dependency injection implementation
*/
class ServiceProvider(private val services: Map<KClass<*>, Stack<ServiceDescriptor>>) {
    companion object {
        val Instance by lazy {
            ServiceCollection.Instance.createServiceProvider()
        }
    }

    fun get(serviceType: KClass<*>): Any? {
        return services[serviceType]?.peek()?.let { getSecure(serviceType, it) }
    }

    fun getAll(serviceType: KClass<*>): Iterable<Any> {
        if(!services.containsKey(serviceType)) {
            return emptyList()
        }

        return services[serviceType]!!.map {
            getSecure(serviceType, it)
        }
    }

    private fun getSecure(serviceType: KClass<*>, serviceDescriptor: ServiceDescriptor): Any {
        val implementation = serviceDescriptor.get(this)

        if(!serviceType.isInstance(implementation)) {
            throw ClassCastException("Instance of ${implementation::class} cannot be cast to $serviceType")
        }

        return implementation
    }
}