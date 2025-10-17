package com.xdfun.autocropharvester.di

import kotlin.reflect.KClass

inline fun <reified T> ServiceProvider.get(): T? {
    return get(T::class) as T
}

fun ServiceProvider.getRequired(serviceType: KClass<*>): Any {
    return get(serviceType) ?: throw NoSuchElementException("No service for $serviceType registered!")
}

inline fun <reified T> ServiceProvider.getRequired(): T {
    return get(T::class) as T? ?: throw NoSuchElementException("No service for ${T::class} registered!")
}

inline fun <reified T> ServiceProvider.getAll(): Iterable<T> {
    val iterable = getAll(T::class)

    iterable.forEach { implementation ->
        if(implementation !is T){
            throw ClassCastException("Instance of ${implementation::class} cannot be cast to ${T::class}")
        }
    }

    return iterable.filterIsInstance<T>()
}

fun ServiceProvider.getAllRequired(serviceType: KClass<*>): Iterable<Any> {
    val iterable = getAll(serviceType)

    if(iterable.none()){
        throw NoSuchElementException("No service for $serviceType registered!")
    }

    return iterable
}

inline fun <reified T> ServiceProvider.getAllRequired(): Iterable<T> {
    val iterable = getAll<T>()

    if(iterable.none()){
        throw NoSuchElementException("No service for ${T::class} registered!")
    }

    return iterable
}

